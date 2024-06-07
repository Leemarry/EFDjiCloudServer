package com.efuav.sdk.cloudapi.firmware;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.events.IEventsErrorCode;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum FirmwareErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    WRONG_TYPE(312001, "一致性升级已完成，但设备未请求。"),

    READY_1_FAILED(312002, "升级失败。请再试一次。"),

    VALIDATION_FAILED(312003, "升级失败。请再试一次。"),

    READY_2_FAILED(312004, "升级失败。请再试一次。"),

    WRONG_PROTOCOL(312010, "升级请求与API不同。"),

    WRONG_PARAMETER(312012, "请检查参数，然后重试。"),

    COMMAND_1_FAILED(312013, "升级失败。请再试一次。"),

    UPDATING(312014, "正在更新设备固件。等待更新完成。"),

    WORKING(312015, "设备在飞行过程中无法升级。请稍候，然后重试。"),

    TRANSMISSION_ERROR(312016, "更新失败。机场和飞机传输错误。重新启动机场和飞机，然后再试一次。"),

    VERSION_CHECK_FAILED(312017, "未能检查版本。"),

    COMMAND_2_FAILED(312018, "升级失败。请再试一次。"),

    COMMAND_3_FAILED(312019, "升级失败。请再试一次。"),

    COMMAND_4_FAILED(312020, "升级失败。请再试一次。"),

    COMMAND_5_FAILED(312021, "升级失败。请再试一次。"),

    AIRCRAFT_NOT_FOUND(312022, "未能接通飞机电源，或飞机未连接。检查飞机是否在机场内，电池是否已安装，机场和飞机是否已连接。"),

    AIRCRAFT_OUTSIDE(312023, "未能将传动杆推回原位。无法更新飞机固件。检查紧急停止按钮是否按下或传动杆是否卡住。"),

    COMMAND_6_FAILED(312024, "升级失败。请再试一次。"),

    DELETE_FAILED(312025, "未能删除旧的固件包。"),

    DECOMPRESSION_FAILED(312026, "无法解压缩离线升级包。"),

    NO_AIRCRAFT_DETECTED(312027, "无法更新固件。机场内未发现飞机。"),

    DEVICE_RESTART_1(312028, "无法更新固件。设备在更新过程中重新启动。"),

    DEVICE_RESTART_2(312029, "正在重新启动设备。无法更新固件。"),

    FOURTH_GENERATION_IS_ENABLE(312030, "启用了飞机增强传输。无法更新固件。请禁用4G传输，然后重试。"),

    LOW_POWER(312704, "飞机电池电量过低。等待飞机充电到20%以上，然后再试一次。"),

    UNKNOWN(-1, "UNKNOWN"),
    ;


    private final String msg;

    private final int code;

    FirmwareErrorCodeEnum(int code, String msg) {
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
    public static FirmwareErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
