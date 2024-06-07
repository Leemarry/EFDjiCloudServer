package com.efuav.sdk.cloudapi.flightarea;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public enum FlightAreaSyncReasonEnum {

    SUCCESS(0, "success"),

    PARSE_FILE_FAILED(1, "无法分析从云中返回的文件信息。"),

    RETRIEVE_FILE_FAILED(2, "未能从飞机末端检索文件信息。"),

    DOWNLOAD_FILE_FAILED(3, "未能从云中下载文件。"),

    LINK_FLIPPING_FAILED(4, "链接翻转失败。"),

    FILE_TRANSMISSION_FAILED(5, "文件传输失败。"),

    DISABLE_FAILED(6, "已归档以禁用。"),

    FILE_DELETION_FAILED(7, "文件删除失败。"),

    FILE_LOADING_FAILED(8, "无法在drone上加载文件。"),

    ENABLE_FAILED(9, "已存档以启用。"),

    TURN_OFF_ENHANCED_FAILED(10, "无法关闭增强图像传输。"),

    POWER_ON_FAILED(11, "无人机开机失败。"),

    CHECK_FAILED(12, "校验和检查失败。"),

    SYNCHRONIZATION_TIMED_OUT(13, "同步异常超时。"),

    ;

    private final int reason;

    private final String msg;

    FlightAreaSyncReasonEnum(int reason, String msg) {
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
    public static FlightAreaSyncReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(FlightAreaSyncReasonEnum.class, reason));
    }

}
