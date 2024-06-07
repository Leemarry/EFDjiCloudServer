package com.efuav.sdk.cloudapi.wayline;

import com.efuav.sdk.common.IErrorInfo;
import com.efuav.sdk.mqtt.events.IEventsErrorCode;
import com.efuav.sdk.mqtt.services.IServicesErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum WaylineErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    SUCCESS(0, "success"),

    WRONG_PARAM(314001, "未能分发任务。稍后再试"),

    MD5_EMPTY(314002, "发出的航线任务md5为空。"),

    WRONG_WAYLINE_FILE(314003, "不支持航线文件格式。检查文件。"),

    DISTRIBUTE_TASK_FAILED_1(314004, "未能分发任务。"),

    MD5_CHECK_FAILED(314005, "航线MD5检查失败。"),

    INITIATE_AIRCRAFT_FAILED_1(314006, "未能启动飞机。重新启动机场，然后重试。"),

    TRANSFER_KMZ_FILE_FAILED(314007, "无法将航线文件从机场分发到飞机。"),

    PREPARATION_TIMED_OUT(314008, "飞机任务准备超时。重新启动机场，然后重试。"),

    INITIATE_AIRCRAFT_FAILED_2(314009, "未能启动飞机。重新启动机场，然后重试。"),

    PERFORM_TASK_FAILED(314010, "无法执行任务。"),

    QUERY_TIMEOUT(314011, "航线执行结果查询超时。"),

    PREPARATION_FAILED_1(314012, "飞机任务准备失败。无法执行任务。重新启动机场，然后重试。"),

    WRONG_KMZ_URL(314013, "获取KMZ下载地址失败。"),

    机场_SYSTEM_ERROR_1(314014, "机场系统错误。无法执行任务。请稍后再试。"),

    CLOSE_FOURTH_GENERATION_FAILED(314015, "未能将AI抽查航线从机场分配到飞机。无法执行任务。请稍后再试，或者重新启动机场然后再试。"),

    PROCESS_KMZ_FILE_FAILED_1(314016, "无法处理飞行航线文件。无法执行任务。检查文件。"),

    MODIFY_KMZ_FILE_FAILED(314017, "修改AI抽查的KMZ文件失败。"),

    AIRCRAFT_RTK_ERROR(314018, "飞机RTK定位错误。无法执行任务。请稍后再试，或者重新启动机场然后再试。"),

    CONVERGE_RTK_FAILED_1(314019, "未能汇聚飞机RTK数据。无法执行任务。请稍后再试，或者重新启动机场然后再试。"),

    AIRCRAFT_POSITION_ERROR(314020, "飞机不在停机坪中间或飞机航向不正确。无法执行任务。检查飞机的位置和航向。"),

    AIRCRAFT_RTK_POSITIONING_ERROR(314021, "飞机RTK定位错误。无法执行任务。请稍后再试，或者重新启动机场然后再试。"),

    MODIFY_KMZ_BREAKPOINT_FILE_FAILED(314022, "修改断点恢复飞行的KMZ文件失败"),

    SETTING_BACKUP_LANDING_POINT_FAILED(316001, "备份着陆点设置失败"),

    SETTING_BACKUP_SAFE_HEIGHT_FAILED(316002, "传输的备份安全高度设置失败"),

    SETTING_TAKEOFF_HEIGHT_FAILED(316003, "起飞高度设置失败。注：飞机的默认安全起飞高度按机场设置为1.8米。飞机起飞后将飞到1.8米，在0-1.8米的起飞过程中不能中断，其他动作只能在起飞后进行。该高度默认由机场使用，不支持修改。其目的是防止人身伤害。"),

    SETTING_OUT_OF_CONTROL_ACTION_FAILED(316004, "失控操作设置失败。"),

    CONVERGE_RTK_FAILED_2(316005, "未能汇聚飞机RTK数据。无法执行任务。重新启动机场，然后重试。"),

    机场_PREPARATION_FAILED(316006, "飞机无法降落在机场上。坞盖关闭或驱动杆推入到位。在机场部署现场检查飞机状态。"),

    INITIATE_AIRCRAFT_FAILED(316007, "未能启动飞机。重新启动机场，然后重试。"),

    OBTAIN_FLIGHT_CONTROL_FAILED(316008, "机场未能获得飞机飞行控制。无法执行任务。确保飞行控制未被遥控器锁定。"),

    LOW_POWER(316009, "飞机电池电量低。无法执行任务。等待飞机充电至50%，然后重试"),

    AIRCRAFT_NOT_DETECTED(316010, "未检测到飞机。无法执行任务。检查飞机是否在机场内并连接到机场，或者重新启动机场并重试。"),

    LANDED_ON_INCORRECT_LOCATION(316011, "飞机降落在错误的位置。检查飞机是否应手动放置在机场部署现场。"),

    FOLDER_COLORING_FAILED(316012, "飞机任务准备失败。文件夹着色失败。"),

    OBTAIN_BATTERY_POWER_FAILED(316013, "查询电池电量失败。"),

    FLIGHT_CONTROL_PUSHING_TIMED_OUT(316014, "飞行控制推送接收超时。"),

    AIRCRAFT_LOCATION_TOO_FAR(316015, "RTK设备校准的飞机位置远离机场。无法执行任务。重新启动机场，然后重试。"),

    LANDING_TIMEOUT(316016, "飞机在机场降落超时。飞机和机场可能会断开连接。查看直播视图，查看飞机是否降落在机场上"),

    OBTAIN_MEDIA_TIMEOUT(316017, "获取飞机媒体文件的数量超时。飞机和机场可能会断开连接。查看直播视图，查看飞机是否降落在机场上"),

    TASK_PERFORMANCE_TIMED_OUT(316018, "任务执行超时。飞机和机场可能会断开连接。查看直播视图，查看飞机是否降落在机场上"),

    CAMERA_COLORING_TIMED_OUT(316019, "相机着色超时"),

    RTK_SOURCE_ERROR(316020, "飞机RTK信号源错误。"),

    RTK_SOURCE_TIMEOUT(316021, "检查飞机RTK信号源超时。"),

    AIRCRAFT_NOT_CONNECTED(316022, "飞机无法返回返航点。检查飞机是否已通电，飞机和机场是否已连接，然后重试"),

    NO_FLIGHT_CONTROL_1(316023, "飞机由管制员B控制，无法返航。从控制器B控制飞机或关闭遥控器电源，然后重试。"),

    WRONG_COMMAND(316024, "飞机未能返航。检查飞机是否已起飞，然后再试一次。"),

    SETTING_AIRCRAFT_PARAMETERS_FAILED(316025, "未能配置飞机参数。请稍后再试，或者重新启动机场然后再试。"),

    EMERGENCY_BUTTON_PRESSED_DOWN(316026, "机场紧急停止按钮按下。无法执行任务。松开按钮，然后重试。"),

    SETTING_AIRCRAFT_PARAMETERS_TIMEOUT(316027, "设置飞机参数超时。请稍后再试，或者重新启动机场然后再试。"),

    FLYING_TO_BACKUP_POINT_1(316029, "机场紧急停止按钮按下。飞往备用着陆点的飞机。确保飞机安全降落，并将飞机放置在机场内"),

    REFRESH_HOME_POINT_FAILED(316030, "刷新返航点失败。请再试一次。"),

    SETTING_RTH_MODE_FAILED(316031, "无法设置返航模式。请再试一次。"),

    LOW_POWER_LANDING_OUTSIDE(316050, "由于电池电量不足，飞机已降落在机场外。请立即检查飞机是否安全降落，并将飞机送回机场。"),

    TASK_ABNORMAL_LANDING_OUTSIDE(316051, "航路任务异常，飞机降落在机场外，请立即检查飞机是否已安全降落，并将飞机送回机场。"),

    FLYING_TO_BACKUP_POINT_2(316052, "航路任务异常，飞机将飞往备用着陆点，请立即检查飞机是否已安全着陆，并将飞机送回机场。"),

    USER_CONTROL_LANDING(316053, "用户控制飞机着陆。"),

    OBTAIN_MEDIA_FAILED(317001, "无法获取飞机媒体文件的数量。"),

    CAMERA_NOT_CONNECTED(317002, "无法格式化飞机存储。确保飞机已通电并连接到机场，并且可以检测到摄像头。或者重新启动飞机，然后再试一次。"),

    FORMAT_AIRCRAFT_STORAGE_FAILED(317003, "无法格式化飞机存储。"),

    FORMAT_MEDIA_FILES_FAILED(317004, "格式化媒体文件失败。"),

    STOP_RECORDING_FAILED(317005, "飞机视频录制终止失败，本次飞行任务的媒体文件可能无法上传。"),

    NOT_IDLE(319001, "无法执行任务。机场正在执行任务或上传问题日志。请等待任务完成或上传日志，然后重试。"),

    机场_SYSTEM_ERROR_2(319002, "机场系统错误。重新启动机场，然后重试。"),

    TASK_ID_NOT_EXIST(319003, "机场中不存在任务ID"),

    TASK_EXPIRE(319004, "任务已过期。"),

    FLIGHTTASK_EXECUTE_COMMAND_TIMEOUT(319005, "执行命令传递超时。无法执行任务。"),

    CANCEL_TASK_FAILED_1(319006, "无法取消任务。任务正在进行中。"),

    EDIT_TASK_FAILED(319007, "无法编辑任务。任务正在进行中。"),

    TIME_NOT_SYNCED(319008, "机场和云时间未同步。机场无法执行任务。"),

    DISTRIBUTE_TASK_FAILED_2(319009, "未能分发任务。请稍后再试，或者重新启动机场然后再试。"),

    VERSION_TOO_EARLY(319010, "机场固件版本太低。无法执行任务。请将机场更新到最新版本，然后重试。"),

    INITIALIZING_机场(319015, "正在初始化机场。无法执行任务。等待初始化完成。"),

    PERFORMING_OTHER_TASK(319016, "机场执行其他任务。无法执行当前任务。"),

    PROCESSING_MEDIA_FILE(319017, "机场处理上次任务中捕获的媒体文件。无法执行当前任务。请稍后再试。"),

    EXPORTING_LOGS(319018, "无法执行任务。机场上传问题日志。请稍后再试。"),

    PULLING_LOGS(319019, "无法执行任务。机场获取问题日志。请稍后再试。"),

    PAUSE_TASK_FAILED(319020, "暂停飞行任务失败。"),

    DISABLE_FLIGHT_CONTROL_FAILED(319021, "无法禁用实时飞行控制。"),

    FLYTO_TASK_FAILED(319022, "FlyTo任务文件。"),

    STOP_FLYTO_TASK_FAILED(319023, "无法停止FlyTo任务。"),

    TAKING_OFF_TASK_FAILED(319024, "One-key 起飞失败."),

    TASK_IN_PREPARATION(319025, "准备中的任务。机场无法执行从云中分发的任务。稍后再试"),

    LOW_POWER_THAN_SET_VALUE(319026, "飞机电池电量低于设定值。无法执行任务。等待充电完成，然后重试。"),

    INSUFFICIENT_STORAGE(319027, "机场或飞机上的存储空间不足。无法执行任务。请等待媒体文件上传到云中，然后重试。"),

    NO_FLIGHT_CONTROL_2(319030, "机场没有飞行控制权。"),

    NO_PAYLOAD_CONTROL(319031, "机场没有有效载荷控制权限"),

    WRONG_POINT_NUMBER(319032, "飞到目标点，点编号错误。"),

    SEQ_NUMBER_SMALLER_THAN_LAST(319033, "DRC—飞行控制失败。程序包序列号小于最后一个。"),

    DELAY_TIME_SMALLER_THAN_SET(319034, "DRC—飞行控制失败。收到的包超时。"),

    EMERGENCY_STOP_FAILED(319035, "紧急停止失败，请重试。"),

    REMOTE_DEBUGGING_MODE(319036, "设备处于远程调试模式。"),

    ONSITE_DEBUGGING_MODE(319037, "设备处于现场调试模式。"),

    UPDATING(319038, "正在更新设备。请稍后再试。"),

    RESUME_TASK_FAILED(319042, "未能恢复飞行。"),

    CANCEL_TASK_FAILED_2(319043, "无法取消RTH。"),

    NO_BREAKPOINT(319044, "任务已完成。无法继续。"),

    EMERGENCY_STOP_STATUS(319045, "DRC—飞行控制失败。飞机暂停。"),

    NOT_IN_WAYLINE(319046, "任务已完成或暂停。无法暂停。"),

    机场_SYSTEM_ERROR_3(319999, "机场系统错误。重新启动机场，然后重试。"),

    TASK_ERROR(321000, "任务错误。请稍后再试，或者重新启动机场然后再试。"),

    PROCESS_KMZ_FILE_FAILED_2(321004, "无法处理飞行航线文件。无法执行任务。检查文件。"),

    MISSING_BREAKPOINT(321005, "航线中缺少断点信息。"),

    TASK_IN_PROGRESS(321257, "任务正在进行中。无法再次启动任务。"),

    STATUS_NOT_SUPPORTED(321258, "无法停止任务。检查飞机状态。"),

    NOT_STARTED_CANNOT_STOP(321259, "任务未启动。无法停止任务。"),

    NOT_STARTED_CANNOT_INTERRUPT(321260, "任务未启动。无法暂停任务。"),

    HEIGHT_LIMIT(321513, "无法执行任务。飞行航线高度大于飞机最大飞行高度。"),

    DISTANCE_LIMIT(321514, "无法执行任务。航线起点或终点在缓冲区或超过距离限制。"),

    GEO_ZONE(321515, "无法执行任务。飞机将飞越GEO区域。"),

    HEIGHT_TOO_LOW(321516, "飞行高度过低。任务已停止。"),

    OBSTACLE_SENSED(321517, "感应到障碍物。任务已停止。"),

    APPROACHED_GEO_ZONE(321519, "飞机接近GEO区域或达到最大距离后自动返航。无法完成任务。"),

    PROPELLER_CHECK_FAILED(321523, "飞机螺旋桨检查失败。螺旋桨可能损坏。请稍后再试。如果问题仍然存在，请联系DJI支持部门更换螺旋桨。"),

    PREPARATION_FAILED_2(321524, "飞机起飞前的准备工作失败，可能是由于飞机无法定位或齿轮错误。请检查飞机的状态。"),

    WEAK_GPS(321769, "飞机卫星定位信号微弱。无法执行任务。重新启动机场，然后重试。"),

    WRONG_GEAR_MODE(321770, "飞机飞行模式错误。无法执行任务。重新启动机场，然后重试。"),

    HOME_POINT_NOT_SET(321771, "未设置飞机返航点。无法执行任务。重新启动机场，然后重试。"),

    LOW_POWER_PERFORM_TASK(321772, "飞机电池电量低。无法执行任务。等待飞机充电至50%，然后重试。"),

    LOW_POWER_RTH(321773, "飞机电池电量低，返回返航点。无法完成任务。"),

    AIRCRAFT_SIGNAL_LOST(321775, "执行任务时飞机信号丢失。"),

    RTK_NOT_READY(321776, "未能汇聚飞机RTK数据。无法执行任务。重新启动机场，然后重试。"),

    NOT_HOVERING(321777, "飞机没有悬停。无法启动任务。"),

    B_CONTROL_PROPELLERS(321778, "无法执行任务。飞机由管制员B控制，螺旋桨启动。"),

    USER_CONTROL(322282, "任务已停止。云用户或控制器B获得的飞机控制。"),

    USER_SEND_RTH(322283, "用户发送的RTH命令。飞机无法完成任务。"),

    WRONG_BREAKPOINT(322539, "断点信息错误。机场无法执行任务"),

    EMPTY_ACTION_LAYER(322594, "动作树的层不能为空。"),

    WRONG_TASK(386535, "任务错误。请稍后再试，或者重新启动机场然后再试。"),

    SET_MEDIA_PRIORITY_FAILED(324030, "设置媒体上传优先级失败，上传队列中不存在该任务。"),

    MEDIA_PRIORITY_COMMAND_TOO_FAST(324031, "设置媒体上传优先级失败，发出命令的动作过快，对上一个命令的响应尚未结束。"),

    MEDIA_PRIORITY_WRONG_PARAMETER(324032, "设置媒体上传优先级失败，参数不正确。"),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    WaylineErrorCodeEnum(int code, String msg) {
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

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", message=" + msg +
                '}';
    }

    /**
     * @param code error code
     * @return enumeration object
     */
    @JsonCreator
    public static WaylineErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
