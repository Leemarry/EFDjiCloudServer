package com.efuav.sdk.cloudapi.flightarea;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class DroneLocation {

    /**
     * 区域唯一ID
     */
    private String areaId;

    /**
     * 到自定义飞行区域边界的距离
     */
    private Float areaDistance;

    /**
     * 是否在定制飞行区
     */
    @JsonProperty("is_in_area")
    private Boolean inArea;

    public String getAreaId() {
        return areaId;
    }

    public DroneLocation setAreaId(String areaId) {
        this.areaId = areaId;
        return this;
    }

    public Float getAreaDistance() {
        return areaDistance;
    }

    public DroneLocation setAreaDistance(Float areaDistance) {
        this.areaDistance = areaDistance;
        return this;
    }

    public Boolean getInArea() {
        return inArea;
    }

    public DroneLocation setInArea(Boolean inArea) {
        this.inArea = inArea;
        return this;
    }
}
