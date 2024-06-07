package com.efuav.sdk.cloudapi.log.api;

import com.efuav.sdk.cloudapi.log.*;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.events.EventsDataRequest;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import com.efuav.sdk.mqtt.services.ServicesPublish;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/28
 */
public abstract class AbstractLogService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * 通知文件上传进度
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FILEUPLOAD_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> fileuploadProgress(TopicEventsRequest<EventsDataRequest<FileUploadProgress>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("fileuploadProgress not implemented");
    }

    /**
     * 获取可上传设备的文件列表
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData<FileUploadListResponse>> fileuploadList(GatewayManager gateway, FileUploadListRequest request) {
        return servicesPublish.publish(
                new TypeReference<FileUploadListResponse>() {
                },
                gateway.getGatewaySn(),
                LogMethodEnum.FILE_UPLOAD_LIST.getMethod(),
                request);
    }

    /**
     * 启动日志文件上传
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData> fileuploadStart(GatewayManager gateway, FileUploadStartRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LogMethodEnum.FILE_UPLOAD_START.getMethod(),
                request);
    }

    /**
     * 更新上传状态
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData> fileuploadUpdate(GatewayManager gateway, FileUploadUpdateRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                LogMethodEnum.FILE_UPLOAD_UPDATE.getMethod(),
                request);
    }

}
