package com.efuav.sdk.cloudapi.control.api;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.control.*;
import com.efuav.sdk.common.BaseModel;
import com.efuav.sdk.common.Common;
import com.efuav.sdk.common.SpringBeanUtils;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.config.version.GatewayTypeEnum;
import com.efuav.sdk.exception.CloudSDKException;
import com.efuav.sdk.mqtt.ChannelName;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.drc.DrcDownPublish;
import com.efuav.sdk.mqtt.drc.DrcUpData;
import com.efuav.sdk.mqtt.drc.TopicDrcRequest;
import com.efuav.sdk.mqtt.events.EventsDataRequest;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import com.efuav.sdk.mqtt.services.ServicesPublish;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public abstract class AbstractControlService {

    @Resource
    private ServicesPublish servicesPublish;

    @Resource
    private DrcDownPublish drcDownPublish;

    /**
     * 飞行结果事件通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_FLY_TO_POINT_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> flyToPointProgress(TopicEventsRequest<FlyToPointProgress> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("flyToPointProgress not implemented");
    }

    /**
     * 一键起飞结果事件通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_TAKEOFF_TO_POINT_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> takeoffToPointProgress(TopicEventsRequest<TakeoffToPointProgress> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("takeoffToPointProgress not implemented");
    }

    /**
     * DRC链路状态通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_DRC_STATUS_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> drcStatusNotify(TopicEventsRequest<DrcStatusNotify> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("drcStatusNotify not implemented");
    }

    /**
     * 操纵手柄控制无效的原因通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_JOYSTICK_INVALID_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> joystickInvalidNotify(TopicEventsRequest<JoystickInvalidNotify> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("joystickInvalidNotify not implemented");
    }

    /**
     * 夺取飞行控制权
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flightAuthorityGrab(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLIGHT_AUTHORITY_GRAB.getMethod());
    }

    /**
     * 有效载荷控制权限抢夺
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> payloadAuthorityGrab(GatewayManager gateway, PayloadAuthorityGrabRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.PAYLOAD_AUTHORITY_GRAB.getMethod(),
                request);
    }

    /**
     * 进入实时飞行控制模式
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> drcModeEnter(GatewayManager gateway, DrcModeEnterRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRC_MODE_ENTER.getMethod(),
                request);
    }

    /**
     * 退出实时飞行控制模式
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> drcModeExit(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRC_MODE_EXIT.getMethod());
    }

    /**
     * 一键起飞
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> takeoffToPoint(GatewayManager gateway, TakeoffToPointRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.TAKEOFF_TO_POINT.getMethod(),
                request);
    }

    /**
     * 飞向目标点
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flyToPoint(GatewayManager gateway, FlyToPointRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLY_TO_POINT.getMethod(),
                request);
    }

    /**
     * 快速更新目标点
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, exclude = GatewayTypeEnum.RC, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> flyToPointUpdate(GatewayManager gateway, FlyToPointUpdateRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLY_TO_POINT_UPDATE.getMethod(),
                request);
    }

    /**
     * 结束飞往目标点的任务
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> flyToPointStop(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.FLY_TO_POINT_STOP.getMethod());
    }

    /**
     * 有效负载控制-切换摄像头模式
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraModeSwitch(GatewayManager gateway, CameraModeSwitchRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_MODE_SWITCH.getMethod(),
                request);
    }

    /**
     * 有效载荷控制-拍摄一张照片
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraPhotoTake(GatewayManager gateway, CameraPhotoTakeRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_PHOTO_TAKE.getMethod(),
                request);
    }

    /**
     * 有效载荷控制-停止拍照
     * 目前仅支持全景照片模式。
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, exclude = GatewayTypeEnum.RC, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> cameraPhotoStop(GatewayManager gateway, CameraPhotoStopRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_PHOTO_STOP.getMethod(),
                request);
    }

    /**
     * 摄像头拍照进度信息事件通知
     * 目前仅支持全景照片模式。
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_CAMERA_PHOTO_TAKE_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> cameraPhotoTakeProgress(TopicEventsRequest<EventsDataRequest<CameraPhotoTakeProgress>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("cameraPhotoTakeProgress not implemented");
    }

    /**
     * 有效负载控制-开始记录
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraRecordingStart(GatewayManager gateway, CameraRecordingStartRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_RECORDING_START.getMethod(),
                request);
    }

    /**
     * 有效负载控制-停止记录
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraRecordingStop(GatewayManager gateway, CameraRecordingStopRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_RECORDING_STOP.getMethod(),
                request);
    }

    /**
     * 有效负载控制-双选项卡变为AIM
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraAim(GatewayManager gateway, CameraAimRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_AIM.getMethod(),
                request);
    }

    /**
     * 有效负载控制-缩放
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraFocalLengthSet(GatewayManager gateway, CameraFocalLengthSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_FOCAL_LENGTH_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-重置万向节
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> gimbalReset(GatewayManager gateway, GimbalResetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.GIMBAL_RESET.getMethod(),
                request);
    }

    /**
     * “lookat”功能是指飞机从当前航向转向实际纬度、经度和高度的指定位置。
     * <p>
     * 对于M30/M30T型号，建议在使用“lookat”功能时使用锁定万向节的方法。
     * <p>
     * 当万向节达到极限时，“观察”功能可能表现异常。
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0, exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraLookAt(GatewayManager gateway, CameraLookAtRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_LOOK_AT.getMethod(),
                request);
    }

    /**
     * 有效负载控制-屏幕拆分
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0, exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> cameraScreenSplit(GatewayManager gateway, CameraScreenSplitRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_SCREEN_SPLIT.getMethod(),
                request);
    }

    /**
     * 有效载荷控制-照片存储设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0, exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> photoStorageSet(GatewayManager gateway, PhotoStorageSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.PHOTO_STORAGE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-视频存储设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0, exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData> videoStorageSet(GatewayManager gateway, VideoStorageSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.VIDEO_STORAGE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-摄像头曝光设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> cameraExposureSet(GatewayManager gateway, CameraExposureSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_EXPOSURE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-摄像头曝光模式设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> cameraExposureModeSet(GatewayManager gateway, CameraExposureModeSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_EXPOSURE_MODE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-摄像头聚焦模式设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> cameraFocusModeSet(GatewayManager gateway, CameraFocusModeSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_FOCUS_MODE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-摄像头焦点值设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> cameraFocusValueSet(GatewayManager gateway, CameraFocusValueSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_FOCUS_VALUE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-空气计量模式设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> irMeteringModeSet(GatewayManager gateway, IrMeteringModeSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.IR_METERING_MODE_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-红外线测量点设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> irMeteringPointSet(GatewayManager gateway, IrMeteringPointSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.IR_METERING_POINT_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-红外线测量区域设置
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> irMeteringAreaSet(GatewayManager gateway, IrMeteringAreaSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.IR_METERING_AREA_SET.getMethod(),
                request);
    }

    /**
     * 有效负载控制-摄像头点对焦点
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> cameraPointFocusAction(GatewayManager gateway, CameraPointFocusActionRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.CAMERA_POINT_FOCUS_ACTION.getMethod(),
                request);
    }

    /**
     * 有效载荷控制
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData> payloadControl(GatewayManager gateway, PayloadControlMethodEnum methodEnum, BaseModel request) {
        try {
            AbstractControlService abstractControlService = SpringBeanUtils.getBean(this.getClass());
            Method method = abstractControlService.getClass().getDeclaredMethod(
                    Common.convertSnake(methodEnum.getPayloadMethod().getMethod()), GatewayManager.class, methodEnum.getClazz());
            return (TopicServicesResponse<ServicesReplyData>) method.invoke(abstractControlService, gateway, request);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new CloudSDKException(e);
        } catch (InvocationTargetException e) {
            throw new CloudSDKException(e.getTargetException());
        }
    }

    /**
     * poi环绕信息的事件通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_POI_STATUS_NOTIFY, outputChannel = ChannelName.OUTBOUND_EVENTS)
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, include = GatewayTypeEnum.DOCK)
    public TopicEventsResponse<MqttReply> poiStatusNotify(TopicEventsRequest<PoiStatusNotify> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("poiStatusNotify not implemented");
    }

    /**
     * 进入poi环绕模式
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, exclude = GatewayTypeEnum.RC, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> poiModeEnter(GatewayManager gateway, PoiModeEnterRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.POI_MODE_ENTER.getMethod(),
                request);
    }

    /**
     * 退出poi环绕模式
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, exclude = GatewayTypeEnum.RC, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> poiModeExit(GatewayManager gateway) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.POI_MODE_EXIT.getMethod());
    }

    /**
     * 然后设置速度
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_2, exclude = GatewayTypeEnum.RC, include = GatewayTypeEnum.DOCK)
    public TopicServicesResponse<ServicesReplyData> poiCircleSpeedSet(GatewayManager gateway, PoiCircleSpeedSetRequest request) {
        return servicesPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.POI_CIRCLE_SPEED_SET.getMethod(),
                request);
    }

    /**
     * DRC飞行控制
     *
     * @param gateway
     * @param request data
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    protected void droneControlDown(GatewayManager gateway, DroneControlRequest request) {
        drcDownPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRONE_CONTROL.getMethod(),
                request);
    }

    /**
     * Drc-up无人机控制结果通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_DRONE_CONTROL)
    public void droneControlUp(TopicDrcRequest<DrcUpData<DroneControlResponse>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("droneControlUp not implemented");
    }

    /**
     * DRC无人机紧急停止
     *
     * @param gateway
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public void droneEmergencyStopDown(GatewayManager gateway) {
        drcDownPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.DRONE_EMERGENCY_STOP.getMethod());
    }

    /**
     * Drc-up无人机紧急停止结果通知
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_DRONE_EMERGENCY_STOP)
    public void droneEmergencyStopUp(TopicDrcRequest<DrcUpData> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("droneEmergencyStopUp not implemented");
    }


    /**
     * DRC心跳
     *
     * @param gateway
     * @param request data
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public void heartBeatDown(GatewayManager gateway, HeartBeatRequest request) {
        drcDownPublish.publish(
                gateway.getGatewaySn(),
                ControlMethodEnum.HEART_BEAT.getMethod(),
                request);
    }

    /**
     * 心跳结果提示
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_HEART_BEAT)
    public void heartBeatUp(TopicDrcRequest<HeartBeatRequest> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("heartBeatUp not implemented");
    }

    /**
     * DRC避障信息推送
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_HSI_INFO_PUSH)
    public void hsiInfoPush(TopicDrcRequest<HsiInfoPush> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("hsiInfoPush not implemented");
    }

    /**
     * 图像传输链路DRC延时信息推送
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_DELAY_INFO_PUSH)
    public void delayInfoPush(TopicDrcRequest<DelayInfoPush> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("delayInfoPush not implemented");
    }

    /**
     * DRC高频osd信息推送
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_DRC_UP_OSD_INFO_PUSH)
    public void osdInfoPush(TopicDrcRequest<OsdInfoPush> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("osdInfoPush not implemented");
    }


}
