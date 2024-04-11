package com.efuav.manage.service.impl;

import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.model.dto.TopologyDeviceDTO;
import com.efuav.manage.model.param.DeviceQueryParam;
import com.efuav.manage.service.IDeviceService;
import com.efuav.manage.service.ITopologyService;
import com.efuav.sdk.cloudapi.device.DeviceDomainEnum;
import com.efuav.sdk.cloudapi.tsa.DeviceTopology;
import com.efuav.sdk.cloudapi.tsa.TopologyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@Service
public class TopologyServiceImpl implements ITopologyService {

    @Autowired
    private IDeviceService deviceService;

    @Override
    public List<TopologyList> getDeviceTopology(String workspaceId) {
        // 查询工作区中所有网关设备的信息。
        List<DeviceDTO> gatewayList = deviceService.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.REMOTER_CONTROL.getDomain()))
                        .build());

        List<TopologyList> topologyList = new ArrayList<>();

        gatewayList.forEach(device -> this.getDeviceTopologyByGatewaySn(device.getDeviceSn())
                .ifPresent(topologyList::add));

        return topologyList;
    }

    public Optional<TopologyList> getDeviceTopologyByGatewaySn(String gatewaySn) {
        Optional<DeviceDTO> dtoOptional = deviceService.getDeviceBySn(gatewaySn);
        if (dtoOptional.isEmpty()) {
            return Optional.empty();
        }
        List<DeviceTopology> parents = new ArrayList<>();
        DeviceDTO device = dtoOptional.get();
        DeviceTopology gateway = deviceService.deviceConvertToTopologyDTO(device);
        parents.add(gateway);

        // 基于无人机sn查询无人机的拓扑数据。
        Optional<TopologyDeviceDTO> deviceTopo = deviceService.getDeviceTopoForPilot(device.getChildDeviceSn());
        List<DeviceTopology> deviceTopoList = new ArrayList<>();
        deviceTopo.ifPresent(deviceTopoList::add);

        return Optional.ofNullable(new TopologyList().setParents(parents).setHosts(deviceTopoList));
    }

}
