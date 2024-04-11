package com.efuav.sdk.cloudapi.airsense.api;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.airsense.AirsenseWarning;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public abstract class AbstractAirsenseService {


    /**
     * 云自定义数据从psdk传输
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_AIRSENSE_WARNING, outputChannel = ChannelName.OUTBOUND_EVENTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    public TopicEventsResponse<MqttReply> airsenseWarning(TopicEventsRequest<List<AirsenseWarning>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("airsenseWarning not implemented");
    }

}
