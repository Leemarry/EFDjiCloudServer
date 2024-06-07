package com.efuav.sdk.cloudapi.wayline;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskBreakPoint {

    /**
     * 断点索引
     */
    @NotNull
    @Min(0)
    private Integer index;

    /**
     * 断点状态
     */
    @NotNull
    private BreakpointStateEnum state;

    /**
     * 当前航线段流程
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float progress;

    /**
     * 航线ID
     */
    @NotNull
    private Integer waylineId;

    public FlighttaskBreakPoint() {
    }

    @Override
    public String toString() {
        return "FlighttaskBreakPoint{" +
                "index=" + index +
                ", state=" + state +
                ", progress=" + progress +
                ", waylineId=" + waylineId +
                '}';
    }

    public Integer getIndex() {
        return index;
    }

    public FlighttaskBreakPoint setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public BreakpointStateEnum getState() {
        return state;
    }

    public FlighttaskBreakPoint setState(BreakpointStateEnum state) {
        this.state = state;
        return this;
    }

    public Float getProgress() {
        return progress;
    }

    public FlighttaskBreakPoint setProgress(Float progress) {
        this.progress = progress;
        return this;
    }

    public Integer getWaylineId() {
        return waylineId;
    }

    public FlighttaskBreakPoint setWaylineId(Integer waylineId) {
        this.waylineId = waylineId;
        return this;
    }
}