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
public enum BreakpointStateEnum {

    WAYLINE_SEGMENT(0, "在航线段上"),

    WAYPOINT(1, "在航路点上");

    private final int state;

    private final String msg;

    BreakpointStateEnum(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    @JsonValue
    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static BreakpointStateEnum find(int state) {
        return Arrays.stream(values()).filter(stateEnum -> stateEnum.state == state).findAny()
                .orElseThrow(() -> new CloudSDKException(BreakpointStateEnum.class, state));
    }
}
