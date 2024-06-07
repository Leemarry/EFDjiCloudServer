package com.efuav.sdk.cloudapi.wayline;

import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class ReadyConditions extends BaseModel {

    /**
     * 电池容量
     * 可执行任务的飞机电池百分比阈值。
     * 任务开始时，飞机电池必须大于“电池容量”。
     */
    @NotNull
    @Min(0)
    @Max(100)
    private Integer batteryCapacity;

    /**
     * 任务可执行期的开始时间
     * 任务可执行周期的起始毫秒时间戳。任务执行时间应晚于“begin_time”。
     */
    @NotNull
    private Long beginTime;

    /**
     * 任务可执行期的结束时间
     * 任务可执行周期的结束毫秒时间戳。任务执行时间应早于“end_time”。
     */
    @NotNull
    private Long endTime;

    public ReadyConditions() {
    }

    @Override
    public String toString() {
        return "ReadyConditions{" +
                "batteryCapacity=" + batteryCapacity +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public ReadyConditions setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
        return this;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public ReadyConditions setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public ReadyConditions setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }
}