package com.efuav.manage.service;

import com.efuav.sdk.cloudapi.tsa.TopologyList;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
public interface ITopologyService {

    /**
     * 获取工作空间中所有设备的拓扑列表，以便进行试点显示。
     * @param workspaceId
     * @return
     */
    List<TopologyList> getDeviceTopology(String workspaceId);

    /**
     * 根据网关sn查询拓扑。
     * @param gatewaySn
     * @return
     */
    Optional<TopologyList> getDeviceTopologyByGatewaySn(String gatewaySn);
}
