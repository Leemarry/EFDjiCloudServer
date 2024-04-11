package com.efuav.sdk.mqtt;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
public interface IMqttTopicService {

    /**
     * 订阅特定主题。
     * @param topics target
     */
    void subscribe(@Header(MqttHeaders.TOPIC) String... topics);

    /**
     * 使用特定的qos订阅特定的主题。
     * @param topic target
     * @param qos   qos
     */
    void subscribe(@Header(MqttHeaders.TOPIC) String topic, int qos);

    /**
     * 取消订阅特定主题。
     * @param topics target
     */
    void unsubscribe(@Header(MqttHeaders.TOPIC) String... topics);

    /**
     * 获取所有订阅的主题。
     * @return topics
     */
    String[] getSubscribedTopic();
}
