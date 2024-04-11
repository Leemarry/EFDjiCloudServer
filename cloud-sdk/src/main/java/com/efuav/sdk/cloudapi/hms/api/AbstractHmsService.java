package com.efuav.sdk.cloudapi.hms.api;

import com.efuav.sdk.cloudapi.hms.Hms;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public abstract class AbstractHmsService {

    /**
     * Reporting of hms
     * @param response
     * @param headers
     * @return
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_HMS)
    public void hms(TopicEventsRequest<Hms> response, MessageHeaders headers) {
        throw new UnsupportedOperationException("hms not implemented");
    }

}
