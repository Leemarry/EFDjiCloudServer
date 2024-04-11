package com.efuav.sdk.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
@MessagingGateway(defaultRequestChannel = ChannelName.OUTBOUND)
public interface IMqttMessageGateway {

    /**
     * 将消息发布到特定主题。
     * @param topic target
     * @param payload   message
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, byte[] payload);

    /**
     * 使用特定的qos将消息推送到特定的主题。
     * @param topic     target
     * @param payload   message
     * @param qos   qos
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, byte[] payload, @Header(MqttHeaders.QOS) int qos);
}
