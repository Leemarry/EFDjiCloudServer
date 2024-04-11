package com.efuav.sdk.mqtt.osd;

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
public class OsdSubscribe {

    public static final String TOPIC = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + "%s" + TopicConst.OSD_SUF;

    @Resource
    private IMqttTopicService topicService;

    public void subscribe(GatewayManager gateway, boolean unsubscribeSubDevice) {
        SDKManager.registerDevice(gateway);
        topicService.subscribe(String.format(TOPIC, gateway.getGatewaySn()));
        if (unsubscribeSubDevice) {
            topicService.unsubscribe(String.format(TOPIC, gateway.getDroneSn()));
            return;
        }
        if (null != gateway.getDroneSn()) {
            topicService.subscribe(String.format(TOPIC, gateway.getDroneSn()));
        }
    }

    public void unsubscribe(GatewayManager gateway) {
        SDKManager.logoutDevice(gateway.getGatewaySn());
        topicService.unsubscribe(String.format(TOPIC, gateway.getGatewaySn()));
        if (null != gateway.getDroneSn()) {
            topicService.unsubscribe(String.format(TOPIC, gateway.getDroneSn()));
        }
    }
}
