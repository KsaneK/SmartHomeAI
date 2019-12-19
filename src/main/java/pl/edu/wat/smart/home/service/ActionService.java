package pl.edu.wat.smart.home.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.dto.*;
import pl.edu.wat.smart.home.entity.Action;
import pl.edu.wat.smart.home.entity.DeviceCapability;
import pl.edu.wat.smart.home.entity.User;
import pl.edu.wat.smart.home.exception.InvalidInputDtoException;
import pl.edu.wat.smart.home.repository.ActionRepo;
import pl.edu.wat.smart.home.repository.DeviceCapabilityRepo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionService extends BaseService {

    @Autowired
    ActionRepo repo;

    @Autowired
    EmailSendService emailSendService;

    @Autowired
    DeviceCapabilityRepo deviceCapabilityRepo;

    @Autowired
    StatusHistoryService statusHistoryService;

    @Value("smarthome.status.url")
    String statusUrl;

    MqttClient client;

    {
        try {
            client = new MqttClient("tcp://localhost:1883", "backend");
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            connectOptions.setKeepAliveInterval(30);
            connectOptions.setAutomaticReconnect(true);
            this.client.connect(connectOptions);
            this.client.subscribe("/#", (s, value) -> {
                String[] topic = s.substring(1).split("/");
                String username = topic[0];
                String device = topic[1];
                String capability = topic[2];
                DtoNewStatusHistory newStatus = new DtoNewStatusHistory(
                        username, device, capability,
                        Integer.parseInt(new String(value.getPayload())));
                statusHistoryService.create(newStatus);
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public List<DtoAction> getAllActions() {
        List<Action> actions = repo.findAllByOwnerId(getCurrentlyLoggedUser().getId());
        return actions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public long addAction(DtoCreateAction dto) {
        Action action = Action.builder()
                .conditionCapability(deviceCapabilityRepo.findByDeviceAndNameAndUser(
                        dto.getCondition_device(), dto.getCondition_capability(), getCurrentlyLoggedUser()))
                .cond(dto.getCondition())
                .conditionValue(dto.getCondition_value())
                .actionCapability(deviceCapabilityRepo.findByDeviceAndNameAndUser(
                        dto.getAction_device(), dto.getAction_capability(), getCurrentlyLoggedUser()))
                .actionValue(dto.getAction_value())
                .build();

        repo.save(action);
        return action.getId();
    }

    private DtoAction convertToDto(Action action) {
        return DtoAction.builder()
                .id(action.getId())
                .condition_capability(getActionDeviceCapability(action.getConditionCapability()))
                .condition_value(action.getConditionValue())
                .condition(action.getCond())
                .action_capability(getActionDeviceCapability(action.getActionCapability()))
                .action_capability_value(action.getActionValue())
                .condition_device(action.getConditionCapability().getDevice().getName())
                .action_device(action.getActionCapability().getDevice().getName())
                .notify(action.isNotify())
                .build();
    }

    private DtoDevice_Capability getActionDeviceCapability(DeviceCapability devCap) {
        return DtoDevice_Capability.builder()
                .component(devCap.getCapability().getComponent())
                .icon(devCap.getIcon())
                .label(devCap.getLabel())
                .name(devCap.getName())
                .build();
    }


    void checkActions(Integer value, DeviceCapability devCap, String username) {
        List<Action> actions = repo.findAllByConditionCapabilityId(devCap.getId());
        for(Action action : actions) {
            if (testCondition(action, value)) {
                if (action.isNotify()) {
                    String message = String.format(
                            "Capability %s of device %s has changed value to %d. Setting value %d of capability %s of device %s.",
                            action.getConditionCapability().getLabel(),
                            action.getConditionCapability().getDevice().getName(),
                            action.getConditionValue(),
                            action.getActionValue(),
                            action.getActionCapability().getLabel(),
                            action.getActionCapability().getDevice().getName());
                    sendEmail(action, message);
                    sendTelegramMessage(action, message);
                }
                performAction(action, username);
            }
        }
    }

    private boolean testCondition(Action action, Integer value) {
        String cond = action.getCond();
        int val = action.getConditionValue();

        switch (cond) {
            case "eq": return value == val;
            case "lt": return value < val;
            case "le": return value <= val;
            case "gt": return value > val;
            case "ge": return value >= val;
            case "ne": return value != val;
        }

        return false;
    }

    private void sendEmail(Action action, String message) {
        User owner = action.getActionCapability().getDevice().getOwner();
        if (owner.isEmailNotification()) {
            emailSendService.sendMessage(owner.getEmail(), "SMARTHOME ALERT", message);
        }
    }

    private void sendTelegramMessage(Action action, String message) {
        User owner = action.getActionCapability().getDevice().getOwner();
        if (owner.isTelegramNotification()) {
            int telegram_id = owner.getTelegramId();
            try {
                URL url = new URL(String.format("https://api.telegram.org/bot%s/sendMessage?text=%s&chat_id=%d",
                        owner.getTelegramToken(), message, telegram_id));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void performAction(Action action, String username) {
        String topic = String.format("/%s/%s/%s", username, action.getActionCapability().getDevice().getSlug(), action.getActionCapability().getName());
        String payload = String.valueOf(action.getActionValue());
        try {
            client.publish(topic, new MqttMessage(payload.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        repo.delete(repo.getOne(id));
    }

    public MqttClient getMqttClient() {
        return client;
    }

    public void setActionNotify(DtoActionNotify dto) {
        Action action = repo.getOne(dto.getAction_id());
        if (action == null || action.getActionCapability().getDevice().getOwner() != getCurrentlyLoggedUser())
            throw new InvalidInputDtoException("Couldn't set action notify.");
        action.setNotify(dto.isNotify());
        repo.save(action);
    }
}
