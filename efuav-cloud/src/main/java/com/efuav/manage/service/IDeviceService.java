package com.efuav.manage.service;

import com.efuav.component.websocket.model.BizCodeEnum;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.efuav.manage.model.dto.TopologyDeviceDTO;
import com.efuav.manage.model.param.DeviceQueryParam;
import com.efuav.sdk.cloudapi.device.ControlSourceEnum;
import com.efuav.sdk.cloudapi.device.DeviceOsdHost;
import com.efuav.sdk.cloudapi.device.DockModeCodeEnum;
import com.efuav.sdk.cloudapi.device.DroneModeCodeEnum;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
public interface IDeviceService {

    /**
     * 飞机离线。
     *
     * @param deviceSn 飞机序列号
     */
    void subDeviceOffline(String deviceSn);

    /**
     * 网关脱机。
     *
     * @param gatewaySn 网关的SN
     */
    void gatewayOffline(String gatewaySn);

    /**
     * 在网关设备联机时订阅网关的主题，并取消订阅子设备的主题。
     *
     * @param gateway
     */
    void gatewayOnlineSubscribeTopic(GatewayManager gateway);

    /**
     * 当无人机上线时，订阅网关和子设备的主题。
     *
     * @param gateway
     */
    void subDeviceOnlineSubscribeTopic(GatewayManager gateway);

    /**
     * 当网关设备脱机时，请取消订阅网关和子设备的主题。
     *
     * @param gateway
     */
    void offlineUnsubscribeTopic(GatewayManager gateway);

    /**
     * 根据不同的查询条件获取设备数据。
     *
     * @param param 查询参数
     * @return
     */
    List<DeviceDTO> getDevicesByParams(DeviceQueryParam param);

    /**
     * web端的业务界面。获取有关此工作区中所有设备的所有信息。
     *
     * @param workspaceId
     * @return
     */
    List<DeviceDTO> getDevicesTopoForWeb(String workspaceId);

    /**
     * 设置无人机的遥控器和有效载荷信息。
     *
     * @param device
     */
    void spliceDeviceTopo(DeviceDTO device);

    /**
     * 根据设备的sn查询设备的信息。
     *
     * @param sn 设备的sn
     * @return
     */
    Optional<TopologyDeviceDTO> getDeviceTopoForPilot(String sn);

    /**
     * 将单个设备信息转换为拓扑对象。
     *
     * @param device
     * @return
     */
    TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device);

    /**
     * 当服务器在同一工作空间中接收到任何设备在线、离线和拓扑更新的请求时，
     * 它还通过websocket向PILOT广播设备在线、离线和拓扑更新的推送，
     * PILOT收到推送后，会再次获取设备拓扑列表。
     *
     * @param workspaceId
     * @param deviceSn
     */
    void pushDeviceOfflineTopo(String workspaceId, String deviceSn);

    /**
     * 当服务器在同一工作空间中接收到任何设备在线、离线和拓扑更新的请求时，
     * 它还通过websocket向PILOT广播设备在线、离线和拓扑更新的推送，
     * PILOT收到推送后，会再次获取设备拓扑列表。
     *
     * @param workspaceId
     * @param gatewaySn
     * @param deviceSn
     */
    void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn);

    /**
     * 更新设备信息。
     *
     * @param deviceDTO
     * @return
     */
    Boolean updateDevice(DeviceDTO deviceDTO);

    /**
     * 将设备绑定到组织和人员。
     *
     * @param device
     */
    Boolean bindDevice(DeviceDTO device);

    /**
     * 在一个工作区中获取绑定设备列表。
     *
     * @param workspaceId
     * @param page
     * @param pageSize
     * @param domain
     * @return
     */
    PaginationData<DeviceDTO> getBoundDevicesWithDomain(String workspaceId, Long page, Long pageSize, Integer domain);

    /**
     * 根据设备的sn取消绑定设备。
     *
     * @param deviceSn
     */
    void unbindDevice(String deviceSn);

    /**
     * 根据设备的sn获取设备信息。
     *
     * @param sn device's sn
     * @return device
     */
    Optional<DeviceDTO> getDeviceBySn(String sn);

    /**
     * 为设备固件更新创建作业。
     *
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    HttpResultResponse createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS);

    /**
     * 设置无人机的属性参数。
     *
     * @param workspaceId
     * @param dockSn
     * @param param
     * @return
     */
    int devicePropertySet(String workspaceId, String dockSn, JsonNode param);

    /**
     * 检查dock的工作状态。
     *
     * @param dockSn
     * @return
     */
    DockModeCodeEnum getDockMode(String dockSn);

    /**
     * 查询飞机的工作状态。
     *
     * @param deviceSn
     * @return
     */
    DroneModeCodeEnum getDeviceMode(String deviceSn);

    /**
     * 检查dock是否处于drc模式。
     *
     * @param dockSn
     * @return
     */
    Boolean checkDockDrcMode(String dockSn);

    /**
     * 检查设备是否具有飞行控制功能。
     *
     * @param gatewaySn
     * @return
     */
    Boolean checkAuthorityFlight(String gatewaySn);

    Integer saveDevice(DeviceDTO device);

    Boolean saveOrUpdateDevice(DeviceDTO device);

    void pushOsdDataToPilot(String workspaceId, String sn, DeviceOsdHost data);

    void pushOsdDataToWeb(String workspaceId, BizCodeEnum codeEnum, String sn, Object data);

    void updateFlightControl(DeviceDTO gateway, ControlSourceEnum controlSource);

    /**
     * 获取所有绑定设备的信息
     *
     * @return
     */
    List<DeviceDTO> getDevices();
}