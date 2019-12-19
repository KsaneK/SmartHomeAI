package pl.edu.wat.smart.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.dto.*;
import pl.edu.wat.smart.home.entity.*;
import pl.edu.wat.smart.home.exception.InvalidInputDtoException;
import pl.edu.wat.smart.home.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService extends BaseService {

    @Autowired
    DeviceRepo repo;

    @Autowired
    DeviceCapabilityRepo deviceCapabilityRepo;

    @Autowired
    CapabilityRepo capabilityRepo;

    @Autowired
    DeviceTypeRepo deviceTypeRepo;

    @Autowired
    ActionRepo actionRepo;

    @Autowired
    StatusHistoryRepo statusHistoryRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DeviceShareRepo deviceShareRepo;


    @Transactional
    public String create(DtoCreateDevice dto) {
        if(repo.countByNameAndOwner(dto.getDevName(), getCurrentlyLoggedUser()) != 0) {
            throw new InvalidInputDtoException("Device name must be unique");
        }

        Device device = new Device();
        device.setName(dto.getDevName());
        device.setSlug(slugify(dto.getDevName()));
        device.setDeviceType(deviceTypeRepo.getOne(dto.getDevType()));
        device.setOwner(getCurrentlyLoggedUser());

        for(int i=0; i<dto.getCapabilities().size(); ++i) {
            DtoCreateDevice_Capability cap = dto.getCapabilities().get(i);
            DeviceCapability deviceCapability = new DeviceCapability();
            deviceCapability.setDevice(device);
            deviceCapability.setCapability(capabilityRepo.getOne(cap.getCapabilityType()));

            deviceCapability.setName(slugify(cap.getCapabilityName()));
            deviceCapability.setLabel(cap.getCapabilityName());
            deviceCapability.setIcon(cap.getCapabilityIcon());

            DeviceCapability savedDeviceCap = deviceCapabilityRepo.save(deviceCapability);

            long mainCapIndex = dto.getMainCapability();
            if(mainCapIndex == i) {
                device.setMainCapability(savedDeviceCap);
            }

        }

        repo.save(device);

        return device.getSlug();
    }

    private String slugify(String str) {
        return str.replaceAll("[^\\w ]", "").trim().replaceAll(" ", "-");
    }

    public List<DtoDevice> getAll() {
        List<Device> devices = repo.findAllByOwnerId(getCurrentlyLoggedUser().getId());
        List<DtoDevice> dtos = new ArrayList<>();

        devices.forEach(dev -> dtos.add(convertDeviceToDto(dev)));

        return dtos;
    }

    public List<DtoDevice> getSharedDevices() {
        List<DtoDevice> dtoDevices = new ArrayList<>();
        List<DeviceShare> shares = deviceShareRepo.findAllBySharedTo(getCurrentlyLoggedUser());
        if (shares == null) return dtoDevices;
        List<Device> devices = new ArrayList<>();
        shares.forEach(share -> devices.add(share.getDevice()));
        devices.forEach(dev -> dtoDevices.add(convertDeviceToDto(dev)));
        return dtoDevices;
    }

    private DtoDevice convertDeviceToDto(Device dev) {
        List<DeviceCapability> capabilities = deviceCapabilityRepo.findByDeviceId(dev.getId());

        DtoDevice dto = new DtoDevice();
        dto.setId(dev.getId());
        dto.setName(dev.getName());
        dto.setSlug(dev.getSlug());
        dto.setType(dev.getDeviceType());
        dto.setOwner(dev.getOwner().getUsername());

        List<DtoDevice_Capability> dtoCapabilities = new ArrayList<>();
        capabilities.forEach(cap -> {
            List<StatusHistory> lastStatus = statusHistoryRepo.findLastByDeviceCapabilityId(
                    cap.getId(), PageRequest.of(0, 1));
            Integer lastValue = lastStatus.size() == 0 ? null : lastStatus.get(0).getValue();

            if(!cap.getDevice().getId().equals(dev.getId()))
                return;

            DtoDevice_Capability capability = new DtoDevice_Capability();
            capability.setName(cap.getName());
            capability.setLabel(cap.getLabel());
            capability.setLast_value(lastValue);

            Capability realCap = cap.getCapability();
            capability.setIcon(cap.getIcon());
            capability.setComponent(realCap.getComponent());

            if(dev.getMainCapability() != null && cap.getId().equals(dev.getMainCapability().getId())) {
                dto.setMainCapability(capability);
            }
            else {
                dtoCapabilities.add(capability);
            }
        });

        dto.setCapabilities(dtoCapabilities);

        return dto;
    }

    public DtoDevice getDeviceBySlug(String slug) {
        Device dev = repo.findBySlugAndOwnerId(slug, getCurrentlyLoggedUser().getId());

        return convertDeviceToDto(dev);
    }

    public DtoDevice getSharedDeviceBySlug(String username, String slug) {
        User owner = userRepo.findByUsername(username);
        Device device = repo.findBySlugAndOwnerId(slug, owner.getId());
        return convertDeviceToDto(device);
    }

    public void delete(Long id) {
        actionRepo.deleteAllByActionCapability_Device_Id(id);
        actionRepo.deleteAllByConditionCapability_Device_Id(id);
        deviceCapabilityRepo.deleteByDeviceId(id);
        repo.delete(repo.findByIdAndOwner(id, getCurrentlyLoggedUser()));
    }

    public Long share(DtoDeviceShareRequest dto) {
        User toShare = userRepo.findByUsername(dto.getUsername());
        if (toShare == null) throw new InvalidInputDtoException(String.format("User %s does not exists", dto.getUsername()));
        Device device = repo.findByIdAndOwner(dto.getDeviceId(), getCurrentlyLoggedUser());
        if (device == null) throw new InvalidInputDtoException("You have no device with given id");
        if (deviceShareRepo.findByDeviceAndSharedTo(device, toShare) != null)
            throw new InvalidInputDtoException(String.format("You already shared this device to %s", toShare.getUsername()));
        DeviceShare deviceShare = new DeviceShare();
        deviceShare.setDevice(device);
        deviceShare.setSharedTo(toShare);
        deviceShareRepo.save(deviceShare);
        return toShare.getId();
    }

    public List<DtoUser> getDeviceSharedTo(Long deviceId) {
        Device device = repo.findByIdAndOwner(deviceId, getCurrentlyLoggedUser());
        if (device == null) throw new InvalidInputDtoException("You have no device with id " + deviceId);
        List<DeviceShare> shares = deviceShareRepo.findAllByDevice(device);
        List<DtoUser> sharedToUsers = new ArrayList<>();
        shares.forEach(share -> sharedToUsers.add(new DtoUser(share.getSharedTo().getId(), share.getSharedTo().getUsername())));
        return sharedToUsers;
    }

    public void deleteDeviceShare(Long deviceId, String username) {
//        Device device = repo.findByIdAndOwner(deviceId, getCurrentlyLoggedUser());
//        if (device == null) throw new InvalidInputDtoException("You have no device with id " + deviceId);
//        User sharedTo = userRepo.findByUsername(username);
//        if (sharedTo == null) throw new InvalidInputDtoException("No user with username " + username + " found");
//        DeviceShare deviceShare = deviceShareRepo.findByDeviceAndSharedTo(device, sharedTo);
//        deviceShareRepo.delete(deviceShare);
        deviceShareRepo.deleteByDevice_OwnerAndDevice_IdAndSharedTo_Username(getCurrentlyLoggedUser(), deviceId, username);
    }

    public void deleteDeviceShare(Long deviceId) {
        deviceShareRepo.deleteByDevice_IdAndSharedTo(deviceId, getCurrentlyLoggedUser());
    }
}
