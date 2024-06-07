package com.efuav.sdk.cloudapi.wayline;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class ProgressExtBreakPoint {

    /**
     * 断点索引
     */
    private Integer index;

    /**
     * 断点状态
     */
    private BreakpointStateEnum state;

    /**
     * 当前路线段流程
     */
    private Float progress;

    /**
     * 路线ID
     */
    private Integer waylineId;

    /**
     * 中断原因
     */
    private FlighttaskBreakReasonEnum breakReason;

    /**
     * 断点纬度
     */
    private Float latitude;

    /**
     * 断点经度
     */
    private Float longitude;

    /**
     * 相对于地球椭球面的断点高度
     */
    private Float height;

    /**
     * 相对于正北（子午线）的偏航角，0至6点钟方向为正值，6至12点钟方向为负值
     */
    private Integer attitudeHead;

    public ProgressExtBreakPoint() {
    }

    @Override
    public String toString() {
        return "FlighttaskBreakPoint{" +
                "index=" + index +
                ", state=" + state +
                ", progress=" + progress +
                ", waylineId=" + waylineId +
                ", breakReason=" + breakReason +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", attitudeHead=" + attitudeHead +
                '}';
    }

    public Integer getIndex() {
        return index;
    }

    public ProgressExtBreakPoint setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public BreakpointStateEnum getState() {
        return state;
    }

    public ProgressExtBreakPoint setState(BreakpointStateEnum state) {
        this.state = state;
        return this;
    }

    public Float getProgress() {
        return progress;
    }

    public ProgressExtBreakPoint setProgress(Float progress) {
        this.progress = progress;
        return this;
    }

    public Integer getWaylineId() {
        return waylineId;
    }

    public ProgressExtBreakPoint setWaylineId(Integer waylineId) {
        this.waylineId = waylineId;
        return this;
    }

    public FlighttaskBreakReasonEnum getBreakReason() {
        return breakReason;
    }

    public ProgressExtBreakPoint setBreakReason(FlighttaskBreakReasonEnum breakReason) {
        this.breakReason = breakReason;
        return this;
    }

    public Float getLatitude() {
        return latitude;
    }

    public ProgressExtBreakPoint setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public ProgressExtBreakPoint setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public ProgressExtBreakPoint setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Integer getAttitudeHead() {
        return attitudeHead;
    }

    public ProgressExtBreakPoint setAttitudeHead(Integer attitudeHead) {
        this.attitudeHead = attitudeHead;
        return this;
    }
}