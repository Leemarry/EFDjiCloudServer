package com.efuav.sdk.cloudapi.debug.api;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.debug.*;
import com.efuav.sdk.common.BaseModel;
import com.efuav.sdk.common.Common;
import com.efuav.sdk.common.SpringBeanUtils;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.config.version.GatewayTypeEnum;
import com.efuav.sdk.exception.CloudSDKException;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public abstract class AbstractDebugService {

    @Resource
    private ServicesPublish servicesPublish;

    /**
     * 打开调试模式
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> debugModeOpen(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DEBUG_MODE_OPEN.getMethod());
    }

    /**
     * 关闭调试模式
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> debugModeClose(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DEBUG_MODE_CLOSE.getMethod());
    }

    /**
     * 打开补光灯
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> supplementLightOpen(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.SUPPLEMENT_LIGHT_OPEN.getMethod());
    }

    /**
     * 关闭补光灯
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> supplementLightClose(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.SUPPLEMENT_LIGHT_CLOSE.getMethod());
    }

    /**
     * 蓄电池维护状态开关
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> batteryMaintenanceSwitch(GatewayManager gateway, BatteryMaintenanceSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.BATTERY_MAINTENANCE_SWITCH.getMethod(),
                request);
    }

    /**
     * 机场空调工作模式开关
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> airConditionerModeSwitch(GatewayManager gateway, AirConditionerModeSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.AIR_CONDITIONER_MODE_SWITCH.getMethod(),
                request);
    }

    /**
     * 机场声光报警开关
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> alarmStateSwitch(GatewayManager gateway, AlarmStateSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.ALARM_STATE_SWITCH.getMethod(),
                request);
    }

    /**
     * 机场储电池模式开关
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> batteryStoreModeSwitch(GatewayManager gateway, BatteryStoreModeSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.BATTERY_STORE_MODE_SWITCH.getMethod(),
                request);
    }

    /**
     * 重新启动机场
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> deviceReboot(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DEVICE_REBOOT.getMethod());
    }

    /**
     * 接通飞机电源
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> droneOpen(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DRONE_OPEN.getMethod());
    }

    /**
     * 关闭飞机电源
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> droneClose(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DRONE_CLOSE.getMethod());
    }

    /**
     * 格式化机场数据
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> deviceFormat(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DEVICE_FORMAT.getMethod());
    }

    /**
     * 格式化飞机数据
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> droneFormat(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.DRONE_FORMAT.getMethod());
    }

    /**
     * 打开机场舱盖
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> coverOpen(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.COVER_OPEN.getMethod());
    }

    /**
     * 关闭机场舱盖
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> coverClose(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.COVER_CLOSE.getMethod());
    }

    /**
     * 打开推杆
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = {GatewayTypeEnum.RC, GatewayTypeEnum.DOCK2})
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> putterOpen(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.PUTTER_OPEN.getMethod());
    }

    /**
     * 关闭推杆
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = {GatewayTypeEnum.RC, GatewayTypeEnum.DOCK2})
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> putterClose(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.PUTTER_CLOSE.getMethod());
    }

    /**
     * 打开充电
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> chargeOpen(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.CHARGE_OPEN.getMethod());
    }

    /**
     * 关闭充电
     *
     * @param gateway
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> chargeClose(GatewayManager gateway) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.CHARGE_CLOSE.getMethod());
    }

    /**
     * 4G增强模式切换
     *
     * @param gateway
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(exclude = GatewayTypeEnum.RC)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> sdrWorkmodeSwitch(GatewayManager gateway, SdrWorkmodeSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.SDR_WORKMODE_SWITCH.getMethod(),
                request);
    }

    /**
     * 远程调试通用接口
     *
     * @param gateway
     * @param methodEnum
     * @param request    data
     * @return services_reply
     */
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> remoteDebug(GatewayManager gateway, DebugMethodEnum methodEnum, BaseModel request) {
        try {
            List<Class> clazz = new ArrayList<>();
            List<Object> args = new ArrayList<>();
            clazz.add(GatewayManager.class);
            args.add(gateway);
            if (Objects.nonNull(request)) {
                clazz.add(request.getClass());
                args.add(request);
            }
            AbstractDebugService abstractDebugService = SpringBeanUtils.getBean(this.getClass());
            Method method = abstractDebugService.getClass().getDeclaredMethod(Common.convertSnake(methodEnum.getMethod()), clazz.toArray(Class[]::new));
            return (TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>>) method.invoke(abstractDebugService, args.toArray());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new CloudSDKException(e);
        } catch (InvocationTargetException e) {
            throw new CloudSDKException(e.getTargetException());
        }
    }

    /**
     * 通知远程调试进度
     *
     * @param request data
     * @param headers The headers for a {@link Message}.
     * @return events_reply
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_EVENTS_CONTROL_PROGRESS, outputChannel = ChannelName.OUTBOUND_EVENTS)
    public TopicEventsResponse<MqttReply> remoteDebugProgress(TopicEventsRequest<EventsDataRequest<RemoteDebugProgress>> request, MessageHeaders headers) {
        throw new UnsupportedOperationException("remoteDebugProgress not implemented");
    }

    /**
     * esim激活
     *
     * @param gateway gateway device
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> esimActivate(GatewayManager gateway, EsimActivateRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.ESIM_ACTIVATE.getMethod(),
                request);
    }

    /**
     * esim与sim交换
     *
     * @param gateway gateway device
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> simSlotSwitch(GatewayManager gateway, SimSlotSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.SIM_SLOT_SWITCH.getMethod(),
                request);
    }

    /**
     * esim 操作员切换
     *
     * @param gateway gateway device
     * @param request data
     * @return services_reply
     */
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    public TopicServicesResponse<ServicesReplyData<RemoteDebugResponse>> esimOperatorSwitch(GatewayManager gateway, EsimOperatorSwitchRequest request) {
        return servicesPublish.publish(
                new TypeReference<RemoteDebugResponse>() {
                },
                gateway.getGatewaySn(),
                DebugMethodEnum.ESIM_OPERATOR_SWITCH.getMethod(),
                request);
    }


}
