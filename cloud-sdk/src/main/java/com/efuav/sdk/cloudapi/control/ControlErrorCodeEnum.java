package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.events.IEventsErrorCode;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum ControlErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    SETTING_RTH_FAILED(327000, "返回原点的高度设置失败。"),

    SETTING_LOST_ACTION_FAILED(327001, "信号丢失操作设置失败。"),

    OBTAIN_CONTROL_FAILED(327002, "未能获得控制权。"),

    DEVICE_OFFLINE(327003, "未能获得控制权。设备离线。"),

    DRAG_LIVESTREAM_VIEW_FAILED(327004, "未能拖动直播视图。"),

    AIM_FAILED(327005, "无法将制表符双击为AIM。"),

    TAKE_PHOTO_FAILED(327006, "拍照失败。"),

    START_RECORDING_FAILED(327007, "无法开始录制。"),

    STOP_RECORDING_FAILED(327008, "无法停止录制。"),

    SWITCH_CAMERA_MODE_FAILED(327009, "无法切换相机模式。"),

    ZOOM_CAMERA_ZOOM_FAILED(327010, "无法使用变焦相机放大/缩小。"),

    IR_CAMERA_ZOOM_FAILED(327011, "无法使用红外相机放大/缩小。"),

    DEVICE_LOCK(327012, "未能获得控制权。设备已锁定。"),

    SETTING_WAYLINE_LOST_ACTION_FAILED(327013, "航线信号丢失动作设置失败。"),

    GIMBAL_REACH_LIMIT(327014, "万向节达到移动极限。"),

    WRONG_LENS_TYPE(327015, "相机镜头类型无效。"),


    DRC_ABNORMAL(514300, "DRC异常。"),

    DRC_HEARTBEAT_TIMED_OUT(514301, "DRC检测信号超时。"),

    DRC_CERTIFICATE_ABNORMAL(514302, "DRC证书异常。"),

    DRC_LINK_LOST(514303, "DRC链接丢失。"),

    DRC_LINK_REFUSED(514304, "DRC链接被拒绝。"),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    ControlErrorCodeEnum(int code, String msg) {
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
    public static ControlErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
