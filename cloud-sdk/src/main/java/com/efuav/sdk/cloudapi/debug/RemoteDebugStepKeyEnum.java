package com.efuav.sdk.cloudapi.debug;

import com.efuav.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/29
 */
public enum RemoteDebugStepKeyEnum {

    GET_BID("get_bid", "Get bid"),

    UPGRADING_PREVENT_REBOOT("upgrading_prevent_reboot", "检查设备是否正在更新"),

    CHECK_WORK_MODE("check_work_mode", "检查是否进入远程调试模式"),

    CHECK_TASK_STATE("check_task_state", "检查DJI 机场是否空闲"),

    LAND_MCU_REBOOT("land_mcu_reboot", "Land MCU重启"),

    RAIN_MCU_REBOOT("rain_mcu_reboot", "气象站MCU重启"),

    CORE_MCU_REBOOT("core_mcu_reboot", "中控MCU重启"),

    SDR_REBOOT("sdr_reboot", "SDR重新启动"),

    WRITE_REBOOT_PARAM_FILE("write_reboot_param_file", "写入重新启动标志"),

    GET_DRONE_POWER_STATE("get_drone_power_state", "获取电池充电状态"),

    CLOSE_PUTTER("close_putter", "关闭推杆"),

    CHECK_WIRED_CONNECT_STATE("check_wired_connect_state", "获取飞机状态"),

    OPEN_DRONE("open_drone", "Open the plane"),

    OPEN_ALARM("open_alarm", "打开声光报警器"),

    CHECK_SCRAM_STATE("check_scram_state", "检查紧急停止开关是否按下"),

    OPEN_COVER("open_cover", "打开舱门"),

    CHECK_DRONE_SDR_CONNECT_STATE("check_drone_sdr_connect_state", "建立SDR无线连接"),

    TURN_ON_DRONE("turn_on_drone", "打开飞机"),

    DRONE_PADDLE_FORWARD("drone_paddle_forward", "Turn on forward paddle"),

    CLOSE_COVER("close_cover", "关闭舱门"),

    DRONE_PADDLE_REVERSE("drone_paddle_reverse", "Turn on reverse paddle"),

    DRONE_PADDLE_STOP("drone_paddle_stop", "停止桨叶旋转"),

    FREE_PUTTER("free_putter", "Free Putter"),

    STOP_CHARGE("stop_charge", "停止充电");

    private final String stepKey;

    private final String message;

    RemoteDebugStepKeyEnum(String stepKey, String message) {
        this.stepKey = stepKey;
        this.message = message;
    }

    @JsonValue
    public String getStepKey() {
        return stepKey;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator
    public static RemoteDebugStepKeyEnum find(String stepKey) {
        return Arrays.stream(values()).filter(stepKeyEnum -> stepKeyEnum.stepKey.equals(stepKey)).findAny()
                .orElseThrow(() -> new CloudSDKException(RemoteDebugStepKeyEnum.class, stepKey));
    }

}
