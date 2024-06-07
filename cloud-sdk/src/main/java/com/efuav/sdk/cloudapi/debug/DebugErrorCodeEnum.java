package com.efuav.sdk.cloudapi.debug;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.events.IEventsErrorCode;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum DebugErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    AIRCRAFT_NO_DONGLE(326002, "DJI Cellular模块未安装在飞机上。"),

    AIRCRAFT_DONGLE_NO_SIM(326003, "飞机的DJI Cellular模块中没有安装SIM卡。"),

    AIRCRAFT_DONGLE_NEED_UPGRADE(326004, "飞机的DJI Cellular模块需要升级，否则无法使用。"),

    ESTABLISH_CONNECTION_FAILED(326005, "飞机的4G传输无法启用，4G传输无法建立连接。请检查4G信号强度，或咨询运营商检查套餐流量和APN设置。"),

    SDR_SWITCH_FAILED(326006, "4G传输开关出现故障，请稍后再试。"),

    WRONG_COMMAND_FORMAT(326007, "命令格式错误。"),

    DOCK_NO_DONGLE(326008, "DJI Cellular模块未安装在底座上。"),

    DOCK_DONGLE_NO_SIM(326009, "机场的DJI Cellular模块中没有安装SIM卡。"),

    DOCK_DONGLE_NEED_UPGRADE(326010, "机场的DJI Cellular模块需要升级，否则无法使用。"),

    COMMAND_NOT_SUPPORTED(514100, "机场错误。重新启动机场，然后重试。"),

    PUSH_DRIVING_RODS_FAILED(514101, "未能将传动杆推入到位。"),

    PULL_DRIVING_RODS_FAILED(514102, "未能将驱动杆拉回。"),

    LOW_POWER_1(514103, "飞机电池电量低。无法执行任务。等待飞机充电至50%，然后重试。"),

    CHARGE_FAILED(514104, "电池充电失败。"),

    STOP_CHARGING_FAILED(514105, "无法停止对电池充电。"),

    REBOOT_DRONE_FAILED(514106, "无法重新启动无人机。"),

    OPEN_DOCK_COVER_FAILED(514107, "无法打开机场舱盖。"),

    CLOSE_DOCK_COVER_FAILED(514108, "无法关闭机场舱盖。"),

    POWER_ON_AIRCRAFT_FAILED(514109, "未能接通飞机电源。"),

    POWER_OFF_AIRCRAFT_FAILED(514110, "未能关闭飞机电源。"),

    OPEN_SLOW_MOTION_FAILED(514111, "打开慢动作模式时螺旋桨错误"),

    CLOSE_SLOW_MOTION_FAILED(514112, "关闭慢动作模式时螺旋桨错误"),

    AIRCRAFT_NOT_FOUND_1(514113, "驱动杆和飞机之间的连接错误。检查飞机是否在机场内，驱动杆是否卡住，充电连接器是否脏污或损坏。"),

    OBTAIN_BATTERY_FAILED(514114, "无法获取飞机电池状态。重新启动机场，然后重试。"),

    DOCK_BUSY(514116, "无法执行操作。机场正在执行其他命令。请稍后再试。"),

    OBTAIN_DOCK_COVER_FAILED(514117, "机场舱盖打开或未完全关闭。重新启动机场并重试。"),

    OBTAIN_DRIVING_RODS_FAILED(514118, "驱动杆向后拉或未推入到位。重新启动机场，然后重试。"),

    TRANSMISSION_ERROR(514120, "机场和飞机断开连接。重新启动机场并重试，或者重新连接机场和飞机。"),

    EMERGENCY_BUTTON_PRESSED_DOWN(514121, "紧急停止按钮按下。松开按钮。"),

    OBTAIN_CHARGING_STATUS_FAILED(514122, "无法获取飞机充电状态。重新启动机场，然后重试。"),

    LOW_POWER_2(514123, "飞机电池电量过低。无法接通飞机电源。"),

    OBTAIN_BATTERY_STATUS_FAILED(514124, "无法获取飞机电池信息。"),

    BATTERY_FULL(514125, "飞机电池电量几乎已满。无法开始充电。当电池电量低于95%时给电池充电。"),

    HEAVY_RAINFALL(514134, "强降雨。无法执行任务。请稍后再试。"),

    HIGH_WIND(514135, "风速过高（≥12 m/s），无法执行任务，请稍后再试。"),

    POWER_SUPPLY_ERROR(514136, "对接电源错误。无法执行任务。请恢复供电，然后重试。"),

    LOW_ENVIRONMENT_TEMPERATURE(514137, "环境温度过低（低于-20°C）。无法执行任务。请稍后再试。"),

    BATTERY_MAINTAINING(514138, "维护飞机电池。无法执行任务。等待维护完成。"),

    MAINTAIN_BATTERY_FAILED(514139, "未能维护飞机电池。无需维护。"),

    SETTING_BATTERY_STORAGE_FAILED(514140, "无法设置电池存储模式。"),

    DOCK_SYSTEM_ERROR(514141, "机场系统错误。重新启动机场，然后重试。"),

    AIRCRAFT_NOT_FOUND_2(514142, "起飞前驱动杆与飞机的连接错误。检查飞机是否在机场内，驱动杆是否卡住，充电连接器是否脏污或损坏。"),

    DRIVING_RODS_ERROR(514143, "驱动杆向后拉或未推入到位。请稍后再试，或者重新启动机场然后再试。"),

    DOCK_COVER_ERROR(514144, "机场舱盖打开或未完全关闭。"),

    ONSITE_DEBUGGING_MODE(514145, "以现场调试模式对接。无法执行当前操作或任务。"),

    REMOTE_DEBUGGING_MODE(514146, "在远程调试模式下停靠。无法执行任务。"),

    FIRMWARE_UPDATING(514147, "正在更新设备固件。无法执行任务。"),

    WORKING(514148, "任务正在进行中。机场无法进入远程调试模式或再次执行任务。"),

    WRONG_STATUS(514149, "机场未处于运行模式，但已发出与运行模式相关的命令。"),

    RESTARTING(514150, "正在重新启动设备。"),

    UPDATING(514151, "正在更新设备固件。"),

    NOT_REMOTE_DEBUGGING_MODE(514153, "机场已退出远程调试模式。无法执行当前操作。"),

    INITIALIZING(514170, "正在初始化机场。无法执行操作。等待初始化完成。"),

    WRONG_PARAMETER(514171, "Cloud命令参数错误。机场无法执行命令。"),

    DISABLE_AC_FAILED(514180, "无法禁用空调制冷或加热。"),

    ENABLE_AC_COOLING_FAILED(514181, "无法启用空调制冷。"),

    ENABLE_AC_HEATING_FAILED(514182, "无法启用交流加热。"),

    ENABLE_AC_DEHUMIDIFYING_FAILED(514183, "无法启用空调除湿。"),

    LOW_TEMPERATURE(514184, "环境温度低于0°C。无法启用交流冷却。"),

    HIGH_TEMPERATURE(514185, "环境温度高于45°C。无法启用交流加热。"),

    UNKNOWN(-1, "未知"),

    ;


    private final String msg;

    private final int code;

    DebugErrorCodeEnum(int code, String msg) {
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
    public static DebugErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
