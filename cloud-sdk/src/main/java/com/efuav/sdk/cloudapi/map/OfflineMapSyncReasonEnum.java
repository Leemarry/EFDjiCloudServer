package com.efuav.sdk.cloudapi.map;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public enum OfflineMapSyncReasonEnum {

    SUCCESS(0, "success"),

    PARSE_FILE_FAILED(1, "无法分析云返回的文件信息。"),

    OBTAIN_DRONE_FILE_FAILED(2, "无法获取飞机文件信息。"),

    DOWNLOAD_FILE_FAILED(3, "未能从云中下载文件。"),

    LINK_ROLLOVER_FAILED(4, "无法滚动链接。"),

    FILE_TRANSFER_FAILED(5, "无法传输文件。"),

    DISABLE_OFFLINE_MAP_FAILED(6, "无法禁用离线映射。"),

    DELETE_FILE_FAILED(7, "未能删除文件。"),

    LOAD_FILE_FAILED(8, "无法在设备端加载文件。"),

    ENABLE_OFFLINE_MAP_FAILED(9, "无法启用离线映射。"),

    ;

    private final int reason;

    private final String msg;

    OfflineMapSyncReasonEnum(int reason, String msg) {
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
    public static OfflineMapSyncReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
            .orElseThrow(() -> new CloudSDKException(OfflineMapSyncReasonEnum.class, reason));
    }

}
