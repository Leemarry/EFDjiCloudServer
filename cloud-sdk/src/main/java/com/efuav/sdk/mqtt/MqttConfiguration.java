package com.efuav.sdk.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 入站消息的客户端配置。
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
@Configuration
@IntegrationComponentScan
public class MqttConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MqttConfiguration.class);

    @Value("${cloud-sdk.mqtt.inbound-topic: }")
    private String inboundTopic;

    @Resource
    private MqttPahoClientFactory mqttClientFactory;

    @Resource(name = ChannelName.INBOUND)
    private MessageChannel inboundChannel;

    /**
     * 入站消息通道的客户端。
     *
     * @return
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                UUID.randomUUID().toString(), mqttClientFactory, inboundTopic.split(","));
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        // 统一使用字节类型
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(1);
        adapter.setOutputChannel(inboundChannel);
        return adapter;
    }

    /**
     * 出站消息通道的客户端。
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = ChannelName.OUTBOUND)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                UUID.randomUUID().toString(), mqttClientFactory);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        // 统一使用字节类型
        converter.setPayloadAsBytes(true);

        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(0);
        messageHandler.setConverter(converter);
        return messageHandler;
    }


    /**
     * 定义一个默认通道来处理无效的消息。
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = ChannelName.DEFAULT)
    public MessageHandler defaultInboundHandler() {
        return message -> {
            log.info("默认通道不处理消息。" +
                    "\n主题: " + message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC) +
                    "\n有效载荷: " + message.getPayload() + "\n");
        };
    }
}
