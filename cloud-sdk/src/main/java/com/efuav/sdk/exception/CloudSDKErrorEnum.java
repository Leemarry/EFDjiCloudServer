package com.efuav.sdk.exception;

import com.efuav.sdk.common.IErrorInfo;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public enum CloudSDKErrorEnum implements IErrorInfo {

    NOT_REGISTERED(210001, "设备未注册。"),

    INVALID_PARAMETER(210002, "无效参数。"),

    DEVICE_TYPE_NOT_SUPPORT(210003, "当前类型的设备不支持此功能。"),

    DEVICE_VERSION_NOT_SUPPORT(210004, "设备的当前版本不支持此功能。"),

    DEVICE_PROPERTY_NOT_SUPPORT(210005, "当前设备不支持此功能。"),

    MQTT_PUBLISH_ABNORMAL(211001, "MQTT消息发送异常。"),

    WEBSOCKET_PUBLISH_ABNORMAL(212001, "webSocket消息发送异常。"),

    WRONG_DATA(220001, "数据超出限制。"),

    UNKNOWN(299999, "SDK未知"),
    ;

    private final int code;

    private final String message;

    CloudSDKErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
