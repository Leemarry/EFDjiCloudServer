package com.efuav.sdk.cloudapi.wayline;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public enum ExitingRTHReasonEnum {

    ADD_JOYSTICK_THROTTLE(0, "添加操纵杆油门"),

    ADD_JOYSTICK_PITCH(1, "添加操纵杆俯仰"),

    INITIALIZATION_FAILED(2, "行为树初始化失败"),

    SURROUNDED_BY_OBSTACLES(3, "被障碍物包围"),

    FLIGHT_RESTRICTION(4, "触发飞行限制"),

    OBSTACLE_IS_TOO_CLOSED(5, "障碍物太近"),

    NO_GPS(6, "无GPS信号"),

    GPS_AND_VIO_ARE_FALSE(7, "GPS和VIO位置的输出标志为假"),

    ERROR_OF_GPS_AND_VIO(8, "GPS与VIO融合位置误差过大"),

    SHORT_DISTANCE_BACKTRACKING(9, "在短距离内回溯"),

    TRIGGER_RTH(10, "短距离触发RTH");

    private final int reason;

    private final String msg;

    ExitingRTHReasonEnum(int reason, String msg) {
        this.reason = reason;
        this.msg = msg;
    }

    @JsonValue
    public int getReason() {
        return reason;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static ExitingRTHReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(ExitingRTHReasonEnum.class, reason));
    }
}
