package com.efuav.sdk.cloudapi.map.api;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.map.MapMethodEnum;
import com.efuav.sdk.cloudapi.map.OfflineMapGetRequest;
import com.efuav.sdk.cloudapi.map.OfflineMapGetResponse;
import com.efuav.sdk.cloudapi.map.OfflineMapSyncProgress;
import com.efuav.sdk.cloudapi.property.DockDroneOfflineMapEnable;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.config.version.GatewayTypeEnum;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.requests.TopicRequestsRequest;
import com.efuav.sdk.mqtt.requests.TopicRequestsResponse;
import com.efuav.sdk.mqtt.services.ServicesPublish;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.efuav.sdk.mqtt.state.TopicStateRequest;
import com.efuav.sdk.mqtt.state.TopicStateResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/19
 */
public abstract class AbstractOfflineMapService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * 关闭离线映射后，离线映射同步将不再自动同步。
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_DRONE_OFFLINE_MAP_ENABLE, outputChannel = ChannelName.OUTBOUND_STATE)
    public TopicStateResponse<MqttReply> dockDroneOfflineMapEnable(TopicStateRequest<DockDroneOfflineMapEnable> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockDroneOfflineMapEnable not implemented");
    }

    /**
     * 主动触发离线地图更新。
     * 机场收到消息后，会在适当的时候主动拉取离线地图信息，并触发离线地图同步机制。
     *
     * @param gateway gateway device
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicServicesResponse<ServicesReplyData> offlineMapUpdate(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                MapMethodEnum.OFFLINE_MAP_UPDATE.getMethod());
    }

    /**
     * 离线映射文件同步状态
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_OFFLINE_MAP_SYNC_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicRequestsResponse<MqttReply> offlineMapSyncProgress(TopicRequestsRequest<OfflineMapSyncProgress> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("offlineMapSyncProgress not implemented");
    }

    /**
     * 机场会主动拉取最新的离线地图文件信息。
     * 根据这些信息，它将检查飞机的离线地图文件名或校验和是否已更改。
     * 一旦发现更改，将触发离线地图同步。
     * 否则，将不会触发同步。
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_OFFLINE_MAP_GET, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicRequestsResponse<MqttReply<OfflineMapGetResponse>> offlineMapGet(TopicRequestsRequest<OfflineMapGetRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("offlineMapGet not implemented");
    }
}
