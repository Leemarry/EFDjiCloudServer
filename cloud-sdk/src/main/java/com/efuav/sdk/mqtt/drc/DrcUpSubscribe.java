package com.efuav.sdk.mqtt.drc;

import com.efuav.sdk.config.version.GatewayManager;
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
public class DrcUpSubscribe {

    @Resource
    private IMqttTopicService topicService;

    public void subscribe(GatewayManager gateway) {
        String drc = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + "%s" + TopicConst.DRC + TopicConst.UP;
        topicService.subscribe(String.format(drc, gateway.getGatewaySn()));
    }
}
