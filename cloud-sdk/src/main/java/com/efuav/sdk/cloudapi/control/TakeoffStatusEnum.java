package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/17
 */
public enum TakeoffStatusEnum {

    TASK_READY("task_ready", "无人机正准备起飞。"),

    WAYLINE_PROGRESS("wayline_progress", "无人机正在起飞。"),

    WAYLINE_FAILED("wayline_failed", "无人机未能起飞。"),

    WAYLINE_OK("wayline_ok", "无人机成功起飞。"),

    WAYLINE_CANCEL("wayline_cancel", "无人机起飞作业已被取消。"),

    TASK_FINISH("task_finish", "无人机起飞工作完成。");

    private final String status;

    private final String message;

    TakeoffStatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TakeoffStatusEnum find(String status) {
        return Arrays.stream(values()).filter(statusEnum -> statusEnum.status.equals(status)).findAny()
                .orElseThrow(() -> new CloudSDKException(TakeoffStatusEnum.class, status));
    }
}
