package com.efuav.sdk.cloudapi.wayline;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public enum WaylineMissionStateEnum {

    DISCONNECT(0, "断开"),

    NOT_SUPPORTED_WAYPOINT(1, "不支持此航路点"),

    WAYLINE_PREPARING(2, "航线准备就绪。可以上传文件，也可以执行上传的文件。"),

    WAYLINE_UPLOADING(3, "正在上传航线文件"),

    DRONE_PREPARING(4, "触发器启动命令。Trgger飞机读取航线。不启动。正在准备中。"),

    ARRIVE_FIRST_WAYPOINT(5, "输入航线并到达第一个航路点"),

    WAYLINE_EXECUTING(6, "执行航线"),

    WAYLINE_BROKEN(7, "航线断了。触发原因：1。用户暂停航线。2.飞行控制异常。"),

    WAYLINE_RECOVER(8, "航线恢复"),

    WAYLINE_END(9, "航线停止"),

    ;

    private final int state;

    private final String msg;

    WaylineMissionStateEnum(int state, String msg) {
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
    public static WaylineMissionStateEnum find(int state) {
        return Arrays.stream(values()).filter(stateEnum -> stateEnum.state == state).findAny()
            .orElseThrow(() -> new CloudSDKException(WaylineMissionStateEnum.class, state));
    }

}
