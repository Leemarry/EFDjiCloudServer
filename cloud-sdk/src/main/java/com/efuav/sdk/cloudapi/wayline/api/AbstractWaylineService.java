package com.efuav.sdk.cloudapi.wayline.api;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.wayline.*;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.common.Common;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.config.version.GatewayTypeEnum;
import com.efuav.sdk.exception.CloudSDKErrorEnum;
import com.efuav.sdk.exception.CloudSDKException;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.events.EventsDataRequest;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import com.efuav.sdk.mqtt.requests.TopicRequestsRequest;
import com.efuav.sdk.mqtt.requests.TopicRequestsResponse;
import com.efuav.sdk.mqtt.services.ServicesPublish;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/19
 */
public abstract class AbstractWaylineService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * 设备退出返回主页（RTH）状态的通知
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_DEVICE_EXIT_HOMING_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> deviceExitHomingNotify(TopicEventsRequest<DeviceExitHomingNotify> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("deviceExitHomingNotify not implemented");
    }

    /**
     * 报告航线任务进度
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHTTASK_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> flighttaskProgress(TopicEventsRequest<EventsDataRequest<FlighttaskProgress>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("flighttaskProgress not implemented");
    }

    /**
     * 任务准备就绪通知
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLIGHTTASK_READY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> flighttaskReady(TopicEventsRequest<FlighttaskReady> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("flighttaskReady not implemented");
    }

    /**
     * 创建航线任务（不推荐）
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(deprecated = CloudSDKVersionEnum.V0_0_1, exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flighttaskCreate(GatewayManager gateway, FlighttaskCreateRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.FLIGHTTASK_CREATE.getMethod(),
                request);
    }

    /**
     * 发布航线任务
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flighttaskPrepare(GatewayManager gateway, FlighttaskPrepareRequest request) {
        validPrepareParam(request);
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.FLIGHTTASK_PREPARE.getMethod(),
                request,
                request.getFlightId());
    }

    /**
     * 执行航线任务
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flighttaskExecute(GatewayManager gateway, FlighttaskExecuteRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.FLIGHTTASK_EXECUTE.getMethod(),
                request,
                request.getFlightId());
    }

    /**
     * 取消航线任务
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flighttaskUndo(GatewayManager gateway, FlighttaskUndoRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.FLIGHTTASK_UNDO.getMethod(),
                request);
    }

    /**
     * 暂停航线任务
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flighttaskPause(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.FLIGHTTASK_PAUSE.getMethod());
    }

    /**
     * 恢复航线任务
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flighttaskRecovery(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.FLIGHTTASK_RECOVERY.getMethod());
    }

    /**
     * 返回起始点（RTH）
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> returnHome(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.RETURN_HOME.getMethod());
    }

    /**
     * 取消返航
     * @param gateway
     * @return  services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> returnHomeCancel(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                WaylineMethodEnum.RETURN_HOME_CANCEL.getMethod());
    }

    /**
     * 获取航线任务资源
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_REQUESTS_FLIGHTTASK_RESOURCE_GET, outputChannel = ChannelName.OUTBOUND_REQUESTS)
    public TopicRequestsResponse<MqttReply<FlighttaskResourceGetResponse>> flighttaskResourceGet(TopicRequestsRequest<FlighttaskResourceGetRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("flighttaskResourceGet not implemented");
    }

    /**
     * 返回主页信息
     * @param request  data
     * @param headers   The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_RETURN_HOME_INFO, outputChannel = ChannelName.OUTBOUND_EVENTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    public TopicRequestsResponse<MqttReply> returnHomeInfo(TopicRequestsRequest<ReturnHomeInfo> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("returnHomeInfo not implemented");
    }

    private void validPrepareParam(FlighttaskPrepareRequest request) {
        if (null == request.getExecuteTime()
                && (TaskTypeEnum.IMMEDIATE == request.getTaskType() || TaskTypeEnum.TIMED == request.getTaskType())) {
            throw new CloudSDKException(CloudSDKErrorEnum.INVALID_PARAMETER, "Execute time must not be null.");
        }
        if (TaskTypeEnum.CONDITIONAL == request.getTaskType()) {
            Common.validateModel(request.getReadyConditions());
        }
    }

}
