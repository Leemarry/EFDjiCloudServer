package com.efuav.sdk.mqtt.property;

import com.efuav.sdk.common.Common;
import com.efuav.sdk.mqtt.Chan;
import com.efuav.sdk.mqtt.ChannelName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Component
public class PropertySetReplyHandler {

    private static final String RESULT_KEY = "result";

    /**
     * 处理来自主题“/property/set_reply”的回复消息。
     * @param message   reply message
     * @throws IOException
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_PROPERTY_SET_REPLY)
    public void propertySetReply(Message<?> message) throws IOException {
        byte[] payload = (byte[])message.getPayload();

        TopicPropertySetResponse receiver = Common.getObjectMapper().readValue(payload, new TypeReference<TopicPropertySetResponse>() {});
        Chan chan = Chan.getInstance(receiver.getTid(), false);
        if (Objects.isNull(chan)) {
            return;
        }
        receiver.setData(PropertySetReplyResultEnum.find(
                Common.getObjectMapper().convertValue(receiver.getData(), JsonNode.class).findValue(RESULT_KEY).intValue()));
        //将消息放入chan对象。
        chan.put(receiver);
    }
}
