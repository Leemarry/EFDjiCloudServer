package com.efuav.sdk.cloudapi.device.api;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.device.*;
import com.efuav.sdk.cloudapi.property.DockDroneCommanderFlightHeight;
import com.efuav.sdk.cloudapi.property.DockDroneCommanderModeLostAction;
import com.efuav.sdk.cloudapi.property.DockDroneRthMode;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.config.version.GatewayTypeEnum;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.osd.TopicOsdRequest;
import com.efuav.sdk.mqtt.state.TopicStateRequest;
import com.efuav.sdk.mqtt.state.TopicStateResponse;
import com.efuav.sdk.mqtt.status.TopicStatusRequest;
import com.efuav.sdk.mqtt.status.TopicStatusResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class AbstractDeviceService {

    /**
     * osd dock
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_DOCK)
    public void osdDock(TopicOsdRequest<OsdDock> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdDock not implemented");
    }

    /**
     * osd dock drone
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_DOCK_DRONE)
    public void osdDockDrone(TopicOsdRequest<OsdDockDrone> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdDockDrone not implemented");
    }

    /**
     * osd远程控制
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_RC)
    public void osdRemoteControl(TopicOsdRequest<OsdRemoteControl> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdRemoteControl not implemented");
    }

    /**
     * osd遥控无人机
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_OSD_RC_DRONE)
    public void osdRcDrone(TopicOsdRequest<OsdRcDrone> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdRcDrone not implemented");
    }

    /**
     * 网关设备+子设备在线
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_ONLINE, outputChannel = ChannelName.OUTBOUND_STATUS)
    public TopicStatusResponse<MqttReply> updateTopoOnline(TopicStatusRequest<UpdateTopo> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("updateTopoOnline not implemented");
    }

    /**
     * 子设备脱机
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return status_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATUS_OFFLINE, outputChannel = ChannelName.OUTBOUND_STATUS)
    public TopicStatusResponse<MqttReply> updateTopoOffline(TopicStatusRequest<UpdateTopo> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("updateTopoOffline not implemented");
    }

    /**
     * 机场和无人机的固件版本更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_FIRMWARE_VERSION)
    public void dockFirmwareVersionUpdate(TopicStateRequest<DockFirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockFirmwareVersionUpdate not implemented");
    }

    /**
     * 遥控器和无人机的固件版本更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_AND_DRONE_FIRMWARE_VERSION)
    public void rcAndDroneFirmwareVersionUpdate(TopicStateRequest<FirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcAndDroneFirmwareVersionUpdate not implemented");
    }

    /**
     * 机场和无人机的无人机控制源更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_CONTROL_SOURCE)
    public void dockControlSourceUpdate(TopicStateRequest<DockDroneControlSource> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockControlSourceUpdate not implemented");
    }

    /**
     * 远程控制和无人机的无人机控制源更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_CONTROL_SOURCE)
    public void rcControlSourceUpdate(TopicStateRequest<RcDroneControlSource> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcControlSourceUpdate not implemented");
    }

    /**
     * 机场和无人机的实时状态更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_LIVE_STATUS)
    public void dockLiveStatusUpdate(TopicStateRequest<DockLiveStatus> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockLiveStatusUpdate not implemented");
    }

    /**
     * 远程控制和无人机的实时状态源更新
     *
     * @param request 请求数据
     * @param headers 消息头
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_LIVE_STATUS)
    public void rcLiveStatusUpdate(TopicStateRequest<RcLiveStatus> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcLiveStatusUpdate not implemented");
    }

    /**
     * 远程控制和无人机的有效载荷固件版本更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_PAYLOAD_FIRMWARE)
    public void rcPayloadFirmwareVersionUpdate(TopicStateRequest<PayloadFirmwareVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcPayloadFirmwareVersionUpdate not implemented");
    }

    /**
     * 无人机的Wpmz固件版本更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_WPMZ_VERSION)
    public void dockWpmzVersionUpdate(TopicStateRequest<DockDroneWpmzVersion> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockWpmzVersionUpdate not implemented");
    }

    /**
     * IR调色板支持的样式
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_THERMAL_SUPPORTED_PALETTE_STYLE)
    public void dockThermalSupportedPaletteStyle(TopicStateRequest<DockDroneThermalSupportedPaletteStyle> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockThermalSupportedPaletteStyle not implemented");
    }

    /**
     * 在最佳RTH模式下，飞机将自动规划最佳返回高度。
     * 当环境和照明不符合视觉系统的要求时（例如晚上阳光直射或晚上没有光线），飞机将在您设定的高度进行直线返回。
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_RTH_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneRthMode(TopicStateRequest<DockDroneRthMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockRthMode not implemented");
    }

    /**
     * 当前RTH高度模式
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_CURRENT_RTH_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCurrentRthMode(TopicStateRequest<DockDroneCurrentRthMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockCurrentRthMode not implemented");
    }

    /**
     * 指向飞行任务失控动作
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_COMMANDER_MODE_LOST_ACTION, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCommanderModeLostAction(TopicStateRequest<DockDroneCommanderModeLostAction> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneCommanderModeLostAction not implemented");
    }

    /**
     * 点对点飞行任务的当前模式
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_CURRENT_COMMANDER_FLIGHT_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCurrentCommanderFlightMode(TopicStateRequest<DockDroneCurrentCommanderFlightMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneCurrentCommanderFlightMode not implemented");
    }

    /**
     * 相对于（机场）起飞点的高度。
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_COMMANDER_FLIGHT_HEIGHT, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneCommanderFlightHeight(TopicStateRequest<DockDroneCommanderFlightHeight> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneCommanderFlightHeight not implemented");
    }

    /**
     * 无人机进入当前状态的原因
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_MODE_CODE_REASON, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneModeCodeReason(TopicStateRequest<DockDroneModeCodeReason> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneModeCodeReason not implemented");
    }

    /**
     * 4g加密狗信息
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_AND_DRONE_DONGLE_INFOS, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dongleInfos(TopicStateRequest<DongleInfos> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dongleInfos not implemented");
    }

    /**
     * 静音模式
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_SILENT_MODE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockSilentMode(TopicStateRequest<DockSilentMode> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockSilentMode not implemented");
    }

}
