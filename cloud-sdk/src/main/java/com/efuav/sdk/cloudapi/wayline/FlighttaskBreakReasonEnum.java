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
public enum FlighttaskBreakReasonEnum {

    NORMAL(0, "无异常"),

    NOT_ID(1, "任务ID不存在。航线任务尚未执行。"),

    UNCOMMON_ERROR(2, "异常错误，请联系技术支持。"),

    ERROR_LOADING_FILE(4, "请求启动/恢复航线任务时加载航线文件时出错，请尝试再次上传文件或联系技术支持。"),

    ERROR_BREAKPOINT_FILE(5, "请求断点信息时查询断点文件失败。请求恢复航线任务时，无法分析断点类型。"),

    INCORRECT_PARAMETER(6, "请求启动/结束航线任务时cmd参数不正确，请求中的协议命令不正确。请求恢复航线任务时，无法分析断点类型。"),

    PARSING_FILE_TIMEOUT(7, "请求启动/恢复航线任务时，解析WPMZ文件超时，请重试。"),

    ALREADY_STARTED(257, "航线已启动，无法重新启动。"),

    UNABLE_TO_INTERRUPT_WAYLINE(258, "在此状态下无法中断航线，只允许在执行状态下暂停航线。"),

    NOT_STARTED(259, "航线尚未开始，无法结束航线。"),

    FLIGHT_MISSION_CONFLICT(261, "飞行任务冲突，无法获得对飞机的控制，在降落和返回期间不允许启动航线。"),

    UNABLE_TO_RESUME_WAYLINE(262, "无法在此状态下恢复航线，只有当航线暂停时才允许。"),

    MAXIMUM_ALTITUDE_LIMIT(513, "飞机超过了最高高度限制。"),

    MAXIMUM_DISTANCE_LIMIT(514, "飞机超过了最大距离限制。"),

    TOO_LOW_HEIGHT(516, "无人机的高度太低。"),

    OBSTACLE_AVOIDANCE(517, "飞机触发障碍物感应。"),

    POOR_RTK(518, "RTK信号差"),

    BOUNDARY_OF_RESTRICTED_ZONE(519, "接近禁区边界。"),

    GEO_ALTITUDE_LIMIT(521, "超过了机场的GEO区域高度限制。"),

    TAKEOFF_REQUEST_FAILED(522, "请求航线起飞失败。"),

    TAKEOFF_EXECUTION_FAILED(523, "起飞任务执行失败。"),

    WAYLINE_MISSION_REQUEST_FAILED(524, "请求航线任务失败。"),

    RTK_FIXING_REQUEST_FAILED(526, "未能请求航线RTK修复任务。"),

    RTK_FIXING_EXECUTION_FAILED(527, "线路RTK修复任务未能运行。"),

    WEAK_GPS(769, "GPS信号微弱。"),

    ERROR_RC_MODE(770, "遥控器未处于N模式，无法启动任务。"),

    HOME_POINT_NOT_REFRESHED(771, "返航点未刷新。"),

    LOW_BATTERY(772, "由于电池电量不足，无法启动任务。"),

    LOW_BATTERY_RTH(773, "线路因电池电量不足而中断，导致返回起始点。"),

    RC_DISCONNECTION(775, "遥控器和飞机之间的断开连接。"),

    ON_THE_GROUND(778, "飞机在地面上螺旋桨旋转，不允许启动航线。"),

    ABNORMAL_VISUAL_STATUS(779, "实时地形跟随过程中的异常视觉状态（例如，太亮、太暗、两侧亮度不一致）。"),

    INVALID_ALTITUDE(780, "用户设置的实时地形跟随高度无效（大于200米或小于30米）。"),

    CALCULATION_ERROR(781, "实时地形跟踪过程中的全局地图计算错误。"),

    STRONG_WINDS_RTH(784, "由于强风导致线路中断，导致返回起始点。"),

    USER_EXIT(1281, "用户退出。"),

    USER_INTERRUPTION(1282, "用户中断。"),

    USER_TRIGGERED_RTH(1283, "用户触发返回主页。"),

    INCORRECT_START_INFORMATION(1539, "起始信息不正确（航路点索引或进度）。"),

    UNSUPPORTED_COORDINATE_SYSTEM(1540, "使用不受支持的坐标系。"),

    UNSUPPORTED_ALTITUDE_MODE(1541, "使用不受支持的海拔模式。"),

    UNSUPPORTED_TRANSITIONAL_WAYLINE_MODE(1542, "使用不受支持的过渡航线模式。"),

    UNSUPPORTED_YAW_MODE(1543, "使用不受支持的偏航模式。"),

    UNSUPPORTED_YAW_DIRECTION_REVERSAL_MODE(1544, "使用不受支持的偏航方向反转模式。"),

    UNSUPPORTED_WAYPOINT_TYPE(1545, "使用不支持的航路点类型。"),

    INVALID_COORDINATED_TURNING_TYPE(1546, "协调转弯类型不能用于起点和终点。"),

    INVALID_GLOBAL_SPEED(1547, "航线全局速度超过合理范围。"),

    WAYPOINT_NUMBER_ABNORMAL(1548, "航路点编号异常。"),

    INVALID_LATITUDE_AND_LONGITUDE(1549, "经纬度数据异常。"),

    ABNORMAL_TURNING_INTERCEPT(1550, "转弯截距异常。"),

    INVALID_SEGMENT_MAXIMUM_SPEED(1551, "航线线段的最大速度超过合理范围。"),

    INVALID_TARGET_SPEED(1552, "线路段目标速度超过合理范围。"),

    INVALID_YAW_ANGLE(1553, "航点偏航角超出合理范围。"),

    BREAKPOINT_INVALID_MISSION_ID(1555, "从断点恢复的输入mission_id错误。"),

    BREAKPOINT_INVALID_PROGRESS_INFORMATION(1556, "从断点输入错误恢复的进度信息。"),

    BREAKPOINT_ERROR_MISSION_STATE(1557, "从断点恢复的任务状态异常。"),

    BREAKPOINT_INVALID_INDEX_INFORMATION(1558, "断点恢复的Wapoint索引信息输入错误。"),

    BREAKPOINT_INCORRECT_LATITUDE_AND_LONGITUDE(1559, "用于从断点恢复的纬度和经度信息不正确。"),

    BREAKPOINT_INVALID_YAW(1560, "从断点恢复期间，航路点的偏航输入错误。"),

    BREAKPOINT_INCORRECT_FLAG_SETTING(1561, "从断点恢复的标志设置不正确。"),

    WAYLINE_GENERATION_FAILED(1563, "航线生成失败。"),

    WAYLINE_EXECUTION_FAILED(1564, "航线执行失败。"),

    WAYLINE_OBSTACLE_SENSING(1565, "线路障碍物感应导致紧急悬停。"),

    UNRECOGNIZED_ACTION_TYPE(1588, "无法识别的操作类型。"),

    DUPLICATE_ACTION_ID(1595, "同一操作组的操作ID不能相同。"),

    ACTION_ID_NOT_65535(1598, "操作ID值不能为65535。"),

    INVALID_NUMBER_OF_ACTION_GROUPS(1602, "操作组的数量超出了合理范围。"),

    ERROR_EFFECTIVE_RANGE(1603, "操作组有效范围错误。"),

    BREAKPOINT_INVALID_ACTION_INDEX(1606, "从断点恢复期间，操作索引超出了合理范围。"),

    BREAKPOINT_TRIGGER_RUNNING_ABNORMAL(1608, "断点信息触发运行结果异常。"),

    BREAKPOINT_DUPLICATE_ACTION_GROUP_ID(1609, "在从断点恢复的过程中，操作组ID信息不能重复。"),

    BREAKPOINT_DUPLICATE_ACTION_GROUP_POSITION(1610, "从断点恢复期间，不能重复操作组位置。"),

    BREAKPOINT_INVALID_ACTION_GROUP_POSITION(1611, "在从断点恢复的过程中，操作组位置超出了合理范围。"),

    BREAKPOINT_INVALID_ACTION_ID(1612, "在恢复过程中，操作ID不在断点信息中。"),

    BREAKPOINT_UNABLE_TO_INTERRUPT(1613, "无法将操作状态修改为在恢复过程中中断。"),

    INCORRECT_BREAKPOINT_INFORMATION(1614, "由于断点信息不正确，恢复失败。"),

    BREAKPOINT_UNRECOGNIZED_ACTION_TYPE(1634, "无法识别的操作类型。"),

    BREAKPOINT_UNRECOGNIZED_TRIGGER_TYPE(1649, "无法识别的触发器类型。"),

    UNKNOWN_ERROR_1(65534, "Unknown error."),

    UNKNOWN_ERROR_2(65535, "Unknown error."),

    ;

    private final int reason;

    private final String msg;

    FlighttaskBreakReasonEnum(int reason, String msg) {
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
    public static FlighttaskBreakReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(FlighttaskBreakReasonEnum.class, reason));
    }
}
