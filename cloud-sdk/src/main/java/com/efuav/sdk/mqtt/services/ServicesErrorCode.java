package com.efuav.sdk.mqtt.services;

import com.efuav.sdk.cloudapi.control.ControlErrorCodeEnum;
import com.efuav.sdk.cloudapi.debug.DebugErrorCodeEnum;
import com.efuav.sdk.cloudapi.firmware.FirmwareErrorCodeEnum;
import com.efuav.sdk.cloudapi.livestream.LiveErrorCodeEnum;
import com.efuav.sdk.cloudapi.log.LogErrorCodeEnum;
import com.efuav.sdk.cloudapi.wayline.WaylineErrorCodeEnum;
import com.efuav.sdk.common.CommonErrorEnum;
import com.efuav.sdk.common.ErrorCodeSourceEnum;
import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.MqttReply;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/7/14
 */
public class ServicesErrorCode implements IErrorInfo {

    private static final int MOD = 100_000;

    private ErrorCodeSourceEnum source;

    private IServicesErrorCode errorCode;

    private boolean success;

    private Integer sourceCode;

    @Override
    public String toString() {
        return "{" +
                "errorCode=" + getCode() +
                ", errorMsg=" + getMessage() +
                '}';
    }

    @JsonCreator
    public ServicesErrorCode(int code) {
        this.sourceCode = code;
        if (MqttReply.CODE_SUCCESS == code) {
            this.success = true;
            this.errorCode = CommonErrorEnum.SUCCESS;
            return;
        }
        this.source = ErrorCodeSourceEnum.find(code / MOD);
        this.errorCode = LiveErrorCodeEnum.find(code % MOD);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = DebugErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = ControlErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = LogErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = FirmwareErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = WaylineErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = CommonErrorEnum.find(code);
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @JsonValue
    public Integer getCode() {
        return sourceCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public ErrorCodeSourceEnum getSource() {
        return source;
    }
}
