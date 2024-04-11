package com.efuav.map.service;

import com.efuav.map.model.dto.DeviceDataStatusDTO;
import com.efuav.map.model.dto.DeviceFlightAreaDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/24
 */
public interface IDeviceDataService {

    List<DeviceDataStatusDTO> getDevicesDataStatus(String workspaceId);

    Optional<DeviceFlightAreaDTO> getDeviceStatus(String workspaceId, String deviceSn);
}
