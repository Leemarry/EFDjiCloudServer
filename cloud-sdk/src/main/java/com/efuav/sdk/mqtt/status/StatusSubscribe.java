package com.efuav.sdk.mqtt.status;

import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.common.SDKManager;
import com.efuav.sdk.mqtt.IMqttTopicService;
import com.efuav.sdk.mqtt.TopicConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Component
public class StatusSubscribe {

    public static final String TOPIC = TopicConst.BASIC_PRE + TopicConst.PRODUCT + "%s" + TopicConst.STATUS_SUF;

    @Resource
    private IMqttTopicService topicService;

    public void subscribe(GatewayManager gateway) {
        SDKManager.registerDevice(gateway);
        topicService.subscribe(String.format(TOPIC, gateway.getGatewaySn()));
    }

    public void subscribeWildcardsStatus() {
        topicService.subscribe(String.format(TOPIC, "+"));
    }

    public void unsubscribe(GatewayManager gateway) {
        SDKManager.logoutDevice(gateway.getGatewaySn());
        topicService.unsubscribe(String.format(TOPIC, gateway.getGatewaySn()));
    }

}
