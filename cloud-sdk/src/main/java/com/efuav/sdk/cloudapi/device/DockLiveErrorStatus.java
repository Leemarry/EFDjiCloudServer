package com.efuav.sdk.cloudapi.device;

import com.efuav.sdk.cloudapi.livestream.LiveErrorCodeEnum;
import com.efuav.sdk.common.ErrorCodeSourceEnum;
import com.efuav.sdk.mqtt.MqttReply;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
public class DockLiveErrorStatus {

    private static final int MOD = 100_000;

    private ErrorCodeSourceEnum source = ErrorCodeSourceEnum.DOCK;

    private LiveErrorCodeEnum errorCode;

    private boolean success;

    @Override
    public String toString() {
        return "{" +
                "errorCode=" + getCode() +
                ", errorMsg=" + getMessage() +
                '}';
    }

    @JsonCreator
    public DockLiveErrorStatus(int code) {
        this.success = MqttReply.CODE_SUCCESS == code;
        this.source = ErrorCodeSourceEnum.find(code / MOD);
        this.errorCode = LiveErrorCodeEnum.find(code % MOD);
    }

    public String getMessage() {
        return errorCode.getMessage();
    }

    @JsonValue
    public Integer getCode() {
        return source.getSource() * MOD + errorCode.getCode();
    }

    public boolean isSuccess() {
        return success;
    }
}