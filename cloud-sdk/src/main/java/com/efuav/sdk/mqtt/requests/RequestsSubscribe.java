package com.efuav.sdk.mqtt.requests;

import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.mqtt.IMqttTopicService;
import com.efuav.sdk.mqtt.TopicConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/24
 */
@Component
public class RequestsSubscribe {

    public static final String TOPIC = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + "%s" + TopicConst.REQUESTS_SUF;

    @Resource
    private IMqttTopicService topicService;

    public void subscribe(GatewayManager gateway) {
        topicService.subscribe(String.format(TOPIC, gateway.getGatewaySn()));
    }

    public void unsubscribe(GatewayManager gateway) {
        topicService.unsubscribe(String.format(TOPIC, gateway.getGatewaySn()));
    }

    public void subscribeWildcardsRequests() {
        topicService.subscribe(TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + "+" + TopicConst.REQUESTS_SUF);
    }
}
