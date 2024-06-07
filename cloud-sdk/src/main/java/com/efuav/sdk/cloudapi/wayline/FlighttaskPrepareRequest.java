package com.efuav.sdk.cloudapi.wayline;

import com.efuav.sdk.annotations.CloudSDKVersion;
import com.efuav.sdk.cloudapi.device.ExitWaylineWhenRcLostEnum;
import com.efuav.sdk.common.BaseModel;
import com.efuav.sdk.config.version.CloudSDKVersionEnum;
import com.efuav.sdk.config.version.GatewayTypeEnum;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskPrepareRequest extends BaseModel {

    /**
     * 任务ID
     */
    @NotNull
    @Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$")
    private String flightId;

    /**
     * 执行时间
     * 任务执行时间的毫秒时间戳。可选字段。
     * 当“task_type”为0或1时，它是必需的。当“task_type”为2时，它不是必需的。
     */
    @Min(123456789012L)
    private Long executeTime;

    /**
     * 任务类型
     * 即时任务和定时任务的执行时间由“execute_time”定义。
     * 条件任务支持由“ready_conditions”定义的任务准备状态条件。
     * 如果在指定的时间段内满足条件，则可以执行该任务。
     * 即时任务具有最高优先级。定时任务和条件任务具有相同的优先级。
     */
    @NotNull
    private TaskTypeEnum taskType;

    /**
     * 航线类型
     */
    @NotNull
    private WaylineTypeEnum waylineType;

    /**
     * 航线文件对象
     */
    @NotNull
    @Valid
    private FlighttaskFile file;

    /**
     * 任务准备情况
     */
    @Valid
    private ReadyConditions readyConditions;

    /**
     * 任务可执行条件
     */
    @Valid
    private ExecutableConditions executableConditions;

    /**
     * 线路断点信息
     */
    @Valid
    private FlighttaskBreakPoint breakPoint;

    /**
     * RTH的高度
     */
    @NotNull
    @Min(20)
    @Max(1500)
    private Integer rthAltitude;

    /**
     * 遥控器失控动作
     * 失控动作：当前固定传输值为0，表示返回原位（RTH）。
     * 注意，该枚举值定义与飞行控制和码头定义不一致，
     * 并且在码头端存在转换。
     */
    @NotNull
    private OutOfControlActionEnum outOfControlAction;

    /**
     * 航线失控动作
     * 与KMZ文件一致
     */
    @NotNull
    private ExitWaylineWhenRcLostEnum exitWaylineWhenRcLost;

    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private RthModeEnum rthMode = RthModeEnum.PRESET_HEIGHT;

    @Valid
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_0)
    private SimulateMission simulateMission;

    @NotNull
    @CloudSDKVersion(since = CloudSDKVersionEnum.V1_0_1, include = GatewayTypeEnum.DOCK2)
    private WaylinePrecisionTypeEnum waylinePrecisionType;

    public FlighttaskPrepareRequest() {
    }

    @Override
    public String toString() {
        return "FlighttaskPrepareRequest{" +
                "flightId='" + flightId + '\'' +
                ", executeTime=" + executeTime +
                ", taskType=" + taskType +
                ", waylineType=" + waylineType +
                ", file=" + file +
                ", readyConditions=" + readyConditions +
                ", executableConditions=" + executableConditions +
                ", breakPoint=" + breakPoint +
                ", rthAltitude=" + rthAltitude +
                ", outOfControlAction=" + outOfControlAction +
                ", exitWaylineWhenRcLost=" + exitWaylineWhenRcLost +
                ", rthMode=" + rthMode +
                ", simulateMission=" + simulateMission +
                ", waylinePrecisionType=" + waylinePrecisionType +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public FlighttaskPrepareRequest setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public FlighttaskPrepareRequest setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public FlighttaskPrepareRequest setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
        return this;
    }

    public WaylineTypeEnum getWaylineType() {
        return waylineType;
    }

    public FlighttaskPrepareRequest setWaylineType(WaylineTypeEnum waylineType) {
        this.waylineType = waylineType;
        return this;
    }

    public FlighttaskFile getFile() {
        return file;
    }

    public FlighttaskPrepareRequest setFile(FlighttaskFile file) {
        this.file = file;
        return this;
    }

    public ReadyConditions getReadyConditions() {
        return readyConditions;
    }

    public FlighttaskPrepareRequest setReadyConditions(ReadyConditions readyConditions) {
        this.readyConditions = readyConditions;
        return this;
    }

    public ExecutableConditions getExecutableConditions() {
        return executableConditions;
    }

    public FlighttaskPrepareRequest setExecutableConditions(ExecutableConditions executableConditions) {
        this.executableConditions = executableConditions;
        return this;
    }

    public FlighttaskBreakPoint getBreakPoint() {
        return breakPoint;
    }

    public FlighttaskPrepareRequest setBreakPoint(FlighttaskBreakPoint breakPoint) {
        this.breakPoint = breakPoint;
        return this;
    }

    public Integer getRthAltitude() {
        return rthAltitude;
    }

    public FlighttaskPrepareRequest setRthAltitude(Integer rthAltitude) {
        this.rthAltitude = rthAltitude;
        return this;
    }

    public OutOfControlActionEnum getOutOfControlAction() {
        return outOfControlAction;
    }

    public FlighttaskPrepareRequest setOutOfControlAction(OutOfControlActionEnum outOfControlAction) {
        this.outOfControlAction = outOfControlAction;
        return this;
    }

    public ExitWaylineWhenRcLostEnum getExitWaylineWhenRcLost() {
        return exitWaylineWhenRcLost;
    }

    public FlighttaskPrepareRequest setExitWaylineWhenRcLost(ExitWaylineWhenRcLostEnum exitWaylineWhenRcLost) {
        this.exitWaylineWhenRcLost = exitWaylineWhenRcLost;
        return this;
    }

    public RthModeEnum getRthMode() {
        return rthMode;
    }

    public FlighttaskPrepareRequest setRthMode(RthModeEnum rthMode) {
        this.rthMode = rthMode;
        return this;
    }

    public SimulateMission getSimulateMission() {
        return simulateMission;
    }

    public FlighttaskPrepareRequest setSimulateMission(SimulateMission simulateMission) {
        this.simulateMission = simulateMission;
        return this;
    }

    public WaylinePrecisionTypeEnum getWaylinePrecisionType() {
        return waylinePrecisionType;
    }

    public FlighttaskPrepareRequest setWaylinePrecisionType(WaylinePrecisionTypeEnum waylinePrecisionType) {
        this.waylinePrecisionType = waylinePrecisionType;
        return this;
    }
}