package com.efuav.sdk.cloudapi.livestream.api;

import com.efuav.sdk.cloudapi.livestream.*;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.services.ServicesPublish;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.efuav.sdk.mqtt.state.TopicStateRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public abstract class AbstractLivestreamService {

    @Resource
    private ServicesPublish servicesPublish;

    private static final long DEFAULT_TIMEOUT = 20_000;

    /**
     * 远程控制的直播功能更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_DOCK_LIVESTREAM_ABILITY_UPDATE)
    public void dockLivestreamAbilityUpdate(TopicStateRequest<DockLivestreamAbilityUpdate> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("dockLivestreamAbilityUpdate not implemented");
    }

    /**
     * dock的直播功能更新
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_RC_LIVESTREAM_ABILITY_UPDATE)
    public void rcLivestreamAbilityUpdate(TopicStateRequest<RcLivestreamAbilityUpdate> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("rcLivestreamAbilityUpdate not implemented");
    }

    /**
     * 开始直播
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData<String>> liveStartPush(GatewayManager gateway, LiveStartPushRequest request) {
        return servicesPublish.publish(
                new TypeReference<String>() {
                },
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_START_PUSH.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }

    /**
     * 停止直播
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData> liveStopPush(GatewayManager gateway, LiveStopPushRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_STOP_PUSH.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }

    /**
     * 设置直播质量
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData> liveSetQuality(GatewayManager gateway, LiveSetQualityRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_SET_QUALITY.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }

    /**
     * 设置直播镜头
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData> liveLensChange(GatewayManager gateway, LiveLensChangeRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LiveStreamMethodEnum.LIVE_LENS_CHANGE.getMethod(),
                request,
                DEFAULT_TIMEOUT);
    }


}
