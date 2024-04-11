package com.efuav.component.mqtt.model;

import lombok.Data;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/18
 */
@Data
public class MqttClientOptions {

    private MqttProtocolEnum protocol;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String clientId;

    private String path;

    /**
     * 客户端连接时要立即订阅的主题。仅用于基本链接。
     */
    private String inboundTopic;
}
