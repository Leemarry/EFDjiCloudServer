package com.efuav.map.service.impl;

import com.efuav.common.error.CommonErrorEnum;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.model.param.DeviceQueryParam;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.manage.service.IDeviceService;
import com.efuav.map.model.dto.DeviceDataStatusDTO;
import com.efuav.map.model.dto.DeviceFlightAreaDTO;
import com.efuav.map.service.IDeviceDataService;
import com.efuav.map.service.IDeviceFlightAreaService;
import com.efuav.sdk.cloudapi.device.DeviceDomainEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/24
 */
@Service
public class DeviceDataServiceImpl implements IDeviceDataService {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private IDeviceFlightAreaService deviceFlightAreaService;

    @Override
    public List<DeviceDataStatusDTO> getDevicesDataStatus(String workspaceId) {
        List<DeviceDTO> devices = deviceService.getDevicesByParams(DeviceQueryParam.builder()
                .domains(List.of(DeviceDomainEnum.DOCK.getDomain())).workspaceId(workspaceId).build());
        if (CollectionUtils.isEmpty(devices)) {
            throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        return devices.stream().map(device -> DeviceDataStatusDTO.builder()
                        .deviceName(device.getDeviceName())
                        .deviceSn(device.getDeviceSn())
                        .nickname(device.getNickname())
                        .online(deviceRedisService.checkDeviceOnline(device.getDeviceSn()))
                        .flightAreaStatus(getDeviceStatus(workspaceId, device.getDeviceSn()).orElse(null))
                        .build())
                .filter(device -> Objects.nonNull(device.getFlightAreaStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DeviceFlightAreaDTO> getDeviceStatus(String workspaceId, String deviceSn) {
        return deviceFlightAreaService.getDeviceFlightAreaByDevice(workspaceId, deviceSn)
                .map(area -> DeviceFlightAreaDTO.builder().syncStatus(area.getSyncStatus()).syncCode(area.getSyncCode()).syncMsg(area.getSyncMsg()).build());
    }
}
