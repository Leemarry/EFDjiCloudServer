package com.efuav.sdk.cloudapi.livestream;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class RcLiveCapacity {

    /**
     * 可用于直播的视频流总数。
     * 指示飞机或设备拥有的所有可用实时视频流的总数。
     */
    private Integer availableVideoNumber;

    /**
     * 可以同时作为实时流的最大视频流总数。
     */
    private Integer coexistVideoNumberMax;

    /**
     * 设备直播功能列表
     */
    private List<RcLiveCapacityDevice> deviceList;

    public RcLiveCapacity() {
    }

    @Override
    public String toString() {
        return "RcLiveCapacity{" +
                "availableVideoNumber=" + availableVideoNumber +
                ", coexistVideoNumberMax=" + coexistVideoNumberMax +
                ", deviceList=" + deviceList +
                '}';
    }

    public Integer getAvailableVideoNumber() {
        return availableVideoNumber;
    }

    public RcLiveCapacity setAvailableVideoNumber(Integer availableVideoNumber) {
        this.availableVideoNumber = availableVideoNumber;
        return this;
    }

    public Integer getCoexistVideoNumberMax() {
        return coexistVideoNumberMax;
    }

    public RcLiveCapacity setCoexistVideoNumberMax(Integer coexistVideoNumberMax) {
        this.coexistVideoNumberMax = coexistVideoNumberMax;
        return this;
    }

    public List<RcLiveCapacityDevice> getDeviceList() {
        return deviceList;
    }

    public RcLiveCapacity setDeviceList(List<RcLiveCapacityDevice> deviceList) {
        this.deviceList = deviceList;
        return this;
    }
}
