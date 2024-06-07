package com.efuav.sdk.cloudapi.firmware.api;

import com.efuav.sdk.cloudapi.firmware.FirmwareMethodEnum;
import com.efuav.sdk.cloudapi.firmware.OtaCreateRequest;
import com.efuav.sdk.cloudapi.firmware.OtaCreateResponse;
import com.efuav.sdk.cloudapi.firmware.OtaProgress;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.events.EventsDataRequest;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import com.efuav.sdk.mqtt.services.ServicesPublish;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/28
 */
public abstract class AbstractFirmwareService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * 固件升级进度
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_OTA_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> otaProgress(TopicEventsRequest<EventsDataRequest<OtaProgress>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("otaProgress not implemented");
    }

    /**
     * 固件升级
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData<OtaCreateResponse>> otaCreate(GatewayManager gateway, OtaCreateRequest request) {
        return servicesPublish.publish(
                new TypeReference<OtaCreateResponse>() {
                },
                gateway.getGatewaySn(),
                FirmwareMethodEnum.OTA_CREATE.getMethod(),
                request);
    }

}
