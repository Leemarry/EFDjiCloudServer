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
public enum ExecutionStepEnum {

    INITIAL(0, "初始状态"),

    PRE_CHECK(1, "飞行前检查：无人机是否正在执行航线？"),

    CHECK_WORK_MODE(2, "启动前检查：机场是否处于退出工作模式？"),

    CHECK_EXECUTION(3, "启动前检查：正在执行路由"),

    CHECK_RETURN(4, "启动前检查：正在返回"),

    PREPARATION(5, "路由执行进入准备状态，等待任务发布开始"),

    OPERATIONAL(6, "机场进入运行状态"),

    OPEN_COVER_PREPARATION(7, "进入启动检查准备和舱门打开准备"),

    WAITING_FOR_FLIGHT_SYSTEM_READINESS(8, "等待飞行系统准备就绪，推送连接建立"),

    WAITING_FOR_RTK(9, "正在等待报告值的RTK源监测"),

    CHECK_RTK_SOURCE(10, "检查RTK源是否来自机场；如果没有，则重置"),

    WAITING_FOR_FLIGHT_CONTROL(11, "等待飞行控制通知"),

    WRESTING_FLIGHT_CONTROL(12, "机场没有控制权；从飞机上夺取控制权"),

    GET_KMZ(13, "获取最新的KMZ URL"),

    DOWNLOAD_KMZ(14, "下载KMZ"),

    KMZ_UPLOADING(15, "KMZ上传"),

    DYE_CONFIGURATION(16, "Dye 配置"),

    SET_DRONE_PARAMETER(17, "飞机起飞参数设置、备降点设置、起飞高度设置、Dye设置"),

    SET_TAKEOFF_PARAMETER(18, "飞机“飞离”起飞参数设置"),

    SET_HOME_POINT(19, "起始点设置"),

    WAYLINE_EXECUTION(20, "触发路由执行"),

    IN_PROGRESS(21, "正在执行路由"),

    RETURN_CHECK_PREPARATION(22, "Entering return check preparation"),

    LADING(23, "飞机降落在机场"),

    CLOSE_COVER(24, "着陆后舱门关闭"),

    EXIT_WORK_MODE(25, "机场退出工作模式"),

    DRONE_ABNORMAL_RECOVERY(26, "机场异常恢复"),

    UPLOADING_FLIGHT_SYSTEM_LOGS(27, "机场上传飞行系统日志"),

    CHECK_RECORDING_STATUS(28, "摄像头录制状态检查"),

    GET_MEDIA_FILES(29, "获取媒体文件的数量"),

    DOCK_ABNORMAL_RECOVERY(30, "机场起飞舱门开启恢复异常"),

    NOTIFY_TASK_RESULTS(31, "通知任务结果"),

    TASK_COMPLETED(32, "任务执行完成；是否根据配置文件启动日志检索"),

    RETRIEVAL_DRONE_LOG_LIST(33, "日志列表检索-飞机列表"),

    RETRIEVAL_DOCK_LOG_LIST(34, "日志列表检索-机场列表检索"),

    UPLOAD_LOG_LIST_RESULTS(35, "日志列表检索-上传日志列表结果"),

    RETRIEVAL_DRONE_LOG(36, "日志检索-检索飞机日志"),

    RETRIEVAL_DOCK_LOG(37, "日志检索-检索机场日志"),

    COMPRESS_DRONE_LOG(38, "日志检索-压缩飞机日志"),

    COMPRESS_DOCK_LOG(39, "日志检索-压缩机场日志"),

    UPLOAD_DRONE_LOG(40, "日志检索-上传飞机日志"),

    UPLOAD_DOCK_LOG(41, "日志检索-上传机场日志"),

    NOTIFY_LOG_RESULTS(42, "日志检索-通知结果"),

    WAITING_FOR_SERVICE_RESPONSE(65533, "完成后等待服务响应"),

    NO_SPECIFIC_STATUS(65534, "没有特定状态"),

    UNKNOWN(65535, "UNKNOWN");

    private final int step;

    private final String msg;

    ExecutionStepEnum(int step, String msg) {
        this.step = step;
        this.msg = msg;
    }

    @JsonValue
    public int getStep() {
        return step;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static ExecutionStepEnum find(int step) {
        return Arrays.stream(values()).filter(stepEnum -> stepEnum.step == step).findAny()
                .orElseThrow(() -> new CloudSDKException(ExecutionStepEnum.class, step));
    }
}
