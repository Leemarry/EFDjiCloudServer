
package com.efuav.sdk.common;

import com.efuav.sdk.mqtt.events.IEventsErrorCode;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum CommonErrorEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    SUCCESS(0, "Success"),

    STATUS_NOT_SUPPORTED(314000, "该设备正在上传日志或执行飞行任务。请稍后再试。"),

    WRONG_PARAMETER(325001, "云命令参数错误。机场无法执行命令。"),

    UNKNOWN(-1, "Unknown");

    private final int code;

    private final String msg;

    CommonErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.msg;
    }

    public static CommonErrorEnum find(int code) {
        return Arrays.stream(values()).filter(error -> error.code == code).findAny().orElse(UNKNOWN);
    }
}
