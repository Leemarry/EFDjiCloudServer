package com.efuav.sdk.mqtt.services;

import com.efuav.sdk.cloudapi.log.FileUploadListResponse;
import com.efuav.sdk.cloudapi.log.LogMethodEnum;
import com.efuav.sdk.common.Common;
import com.efuav.sdk.mqtt.Chan;
import com.efuav.sdk.mqtt.ChannelName;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class ServicesReplyHandler {

    /**
     * 处理主题“/services_reply”中的回复消息。
     * @param message   reply message
     * @throws IOException
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_SERVICES_REPLY)
    public void servicesReply(Message<?> message) throws IOException {
        byte[] payload = (byte[])message.getPayload();

        TopicServicesResponse<ServicesReplyReceiver> receiver = Common.getObjectMapper()
                .readValue(payload, new TypeReference<TopicServicesResponse<ServicesReplyReceiver>>() {});
        Chan chan = Chan.getInstance(receiver.getTid(), false);
        if (Objects.isNull(chan)) {
            return;
        }
        if (LogMethodEnum.FILE_UPLOAD_LIST.getMethod().equals(receiver.getMethod())) {
            receiver.getData().setOutput(Common.getObjectMapper().convertValue(receiver.getData(),
                    new TypeReference<FileUploadListResponse>() {}));
        }
        chan.put(receiver);
    }
}
