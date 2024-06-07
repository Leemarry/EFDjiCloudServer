package com.efuav.sdk.cloudapi.log;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.events.IEventsErrorCode;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum LogErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    DEVICE_RESTART(324001, "设备重新启动会中断日志导出。"),

    EXPORT_TIMEOUT(324012, "压缩日志超时。选择的日志太多。取消选择一些日志，然后重试。"),

    PULL_FAILED(324013, "无法获取设备日志列表。请稍后再试。"),

    EMPTY_LOG_LIST(324014, "设备日志列表为空。刷新页面或重新启动机场，然后重试。"),

    AIRCRAFT_SHUTDOWN(324015, "飞机断电或未连接。无法获取日志列表。确保飞机在机场内。远程接通飞机电源，然后再试一次。"),

    INSUFFICIENT_STORAGE_SPACE(324016, "机场存储空间不足。无法压缩日志。请清除空间或稍后再试。"),

    NO_LOG(324017, "无法压缩日志。无法获取所选飞机的日志。刷新页面或重新启动机场，然后重试。"),

    COMPRESSION_FAILED(324018, "未能压缩日志并提交问题报告。请稍后再试，或者重新启动机场然后再试。"),

    UPLOAD_FAILED(324019, "由于机场网络异常，日志上传失败。请稍后重试。"),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    LogErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    /**
     * @param code error code
     * @return enumeration object
     */
    public static LogErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
