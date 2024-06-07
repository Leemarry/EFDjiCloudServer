package com.efuav.sdk.cloudapi.livestream;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum LiveErrorCodeEnum implements IServicesErrorCode, IErrorInfo {

    SUCCESS(0, "Success"),

    NO_AIRCRAFT(13001, "没有飞机。"),

    NO_CAMERA(13002, "没有摄像头。"),

    LIVE_STREAM_ALREADY_STARTED(13003, "摄像机已开始直播。"),

    FUNCTION_NOT_SUPPORT(13004, "不支持该功能。"),

    STRATEGY_NOT_SUPPORT(13005, "该策略不受支持。"),

    NOT_IN_CAMERA_INTERFACE(13006, "当前应用程序不在相机界面中。"),

    NO_FLIGHT_CONTROL(13007, "遥控器没有飞行控制权，无法响应控制命令"),

    NO_STREAM_DATA(13008, "当前应用程序没有流数据。"),

    TOO_FREQUENT(13009, "操作过于频繁。"),

    ENABLE_FAILED(13010, "请检查直播服务是否正常。"),

    NO_LIVE_STREAM(13011, "目前没有直播。"),

    SWITCH_NOT_SUPPORT(13012, "直播中已经有另一台摄像机。不支持直接切换流。"),

    URL_TYPE_NOT_SUPPORTED(13013, "不支持此url类型。"),

    ERROR_PARAMETERS(13014, "直播参数异常或不完整。"),

    NETWORK_CONGESTION(13015, "请检查网络。"),

    ERROR_FRAME(13016, "实时解码失败。"),

    DEVICE_UNKNOWN(13099, "设备内部出现未知错误。"),

    UNKNOWN(-1, "UNKNOWN"),
    ;


    private final String msg;

    private final int code;

    LiveErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    /**
     * @param code error code
     * @return enumeration object
     */
    public static LiveErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
