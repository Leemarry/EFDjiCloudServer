package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/17
 */
public enum DrcStatusErrorEnum implements IErrorInfo {

    SUCCESS(0, "success"),

    MQTT_ERR(514300, "MQTT连接错误。"),

    HEARTBEAT_TIMEOUT(514301, "心跳超时，机场断开连接。"),

    MQTT_CERTIFICATE_ERR(514302, "MQTT证书异常，连接失败。"),

    MQTT_LOST(514303, "机场网络异常，MQTT连接丢失。"),

    MQTT_REFUSE(514304, "与MQTT服务器的对接连接被拒绝。");

    private final String msg;

    private final int code;

    DrcStatusErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DrcStatusErrorEnum find(int code) {
        return Arrays.stream(values()).filter(error -> error.code == code).findAny()
                .orElseThrow(() -> new CloudSDKException(DrcStatusErrorEnum.class, code));
    }
}
