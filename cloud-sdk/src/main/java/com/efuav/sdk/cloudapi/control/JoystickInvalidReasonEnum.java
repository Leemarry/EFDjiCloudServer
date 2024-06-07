package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public enum JoystickInvalidReasonEnum {

    RC_LOST(0, "遥控器丢失。"),

    BATTERY_LOW_GO_HOME(1, "由于电池电量不足，无人机自动返回起始点。"),

    BATTERY_SUPER_LOW_LANDING(2, "由于电池电量严重不足，无人机自动降落。"),

    NEAR_BOUNDARY(3, "无人机在禁飞区附近。"),

    RC_AUTHORITY(4, "遥控器获取控制权限。");

    private final int reason;

    private final String message;

    JoystickInvalidReasonEnum(int reason, String message) {
        this.reason = reason;
        this.message = message;
    }

    @JsonValue
    public int getVal() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static JoystickInvalidReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(JoystickInvalidReasonEnum.class, reason));
    }
}
