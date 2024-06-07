package com.efuav.sdk.cloudapi.organization.api;

import com.efuav.sdk.cloudapi.organization.*;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.requests.TopicRequestsRequest;
import com.efuav.sdk.mqtt.requests.TopicRequestsResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public abstract class AbstractOrganizationService {

    /**
     * 获取组织绑定信息
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_BIND_STATUS, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<AirportBindStatusResponse>> airportBindStatus(
            TopicRequestsRequest<AirportBindStatusRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airportBindStatus not implemented");
    }

    /**
     * 搜索设备绑定到的组织信息
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_GET, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<AirportOrganizationGetResponse>> airportOrganizationGet(
            TopicRequestsRequest<AirportOrganizationGetRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airportOrganizationGet not implemented");
    }

    /**
     * 设备绑定到组织
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_AIRPORT_ORGANIZATION_BIND, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<AirportOrganizationBindResponse>> airportOrganizationBind(
            TopicRequestsRequest<AirportOrganizationBindRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airportOrganizationBind not implemented");
    }
}
