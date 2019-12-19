package pl.edu.wat.smart.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.dto.DtoNewStatusHistory;
import pl.edu.wat.smart.home.dto.DtoStatusHistory;
import pl.edu.wat.smart.home.entity.*;
import pl.edu.wat.smart.home.exception.InvalidInputDtoException;
import pl.edu.wat.smart.home.repository.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusHistoryService extends BaseService {

    @Autowired
    StatusHistoryRepo repo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DeviceRepo deviceRepo;

    @Autowired
    DeviceCapabilityRepo deviceCapabilityRepo;

    @Autowired
    DeviceShareRepo deviceShareRepo;

    @Autowired
    ActionService actionService;

    public void create(DtoNewStatusHistory dto) {
        User user = userRepo.findByUsername(dto.getUsername());
        Device device = deviceRepo.findBySlugAndOwnerId(dto.getDevice(), user.getId());
        if (device == null) {
            return;
        }
        DeviceCapability deviceCapability = deviceCapabilityRepo.findByNameAndDeviceId(dto.getCapability(), device.getId());
        if (deviceCapability == null) {
            return;
        }

        StatusHistory status = new StatusHistory();
        status.setDate(new Date());
        status.setValue(dto.getMessage());
        status.setDeviceCapability(deviceCapability);

        repo.save(status);

        actionService.checkActions(dto.getMessage(), deviceCapability, user.getUsername());
    }

    public List<DtoStatusHistory> getStatusHistoryForUserAndDeviceCapability(User user, String deviceSlug, String capName) {
        Device device = deviceRepo.findBySlugAndOwnerId(deviceSlug, user.getId());
        DeviceCapability deviceCapability = deviceCapabilityRepo.findByNameAndDeviceId(capName, device.getId());
        List<StatusHistory> statusHistoryList = repo.findLastByDeviceCapabilityId(deviceCapability.getId(),
                PageRequest.of(0, 100));
        statusHistoryList.sort((o1, o2) -> o1.getDate().after(o2.getDate()) ? 1 : 0);
        return statusHistoryList.stream().map(status -> DtoStatusHistory.builder()
                .date(status.getDate())
                .value(status.getValue())
                .build())
                .collect(Collectors.toList());
    }

    public List<DtoStatusHistory> getStatusHistoryForDevice(String deviceSlug, String capName) {
        return getStatusHistoryForUserAndDeviceCapability(getCurrentlyLoggedUser(), deviceSlug, capName);
    }

    public List<DtoStatusHistory> getStatusHistoryForDevice(String owner, String devSlug, String capName) {
        User devOwner = userRepo.findByUsername(owner);
        if (devOwner == null) throw new InvalidInputDtoException("No user with name " + owner);
        Device device = deviceRepo.findBySlugAndOwnerId(devSlug, devOwner.getId());
        DeviceShare share = deviceShareRepo.findByDeviceAndSharedTo(device, getCurrentlyLoggedUser());
        if (share == null) throw new InvalidInputDtoException("No permission to device.");
        return getStatusHistoryForUserAndDeviceCapability(devOwner, devSlug, capName);
    }
}