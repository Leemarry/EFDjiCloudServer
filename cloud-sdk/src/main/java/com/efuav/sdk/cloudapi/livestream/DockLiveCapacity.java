package com.efuav.sdk.cloudapi.livestream;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class DockLiveCapacity {

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
    private List<DockLiveCapacityDevice> deviceList;

    public DockLiveCapacity() {
    }

    @Override
    public String toString() {
        return "DockLiveCapacity{" +
                "availableVideoNumber=" + availableVideoNumber +
                ", coexistVideoNumberMax=" + coexistVideoNumberMax +
                ", deviceList=" + deviceList +
                '}';
    }

    public Integer getAvailableVideoNumber() {
        return availableVideoNumber;
    }

    public DockLiveCapacity setAvailableVideoNumber(Integer availableVideoNumber) {
        this.availableVideoNumber = availableVideoNumber;
        return this;
    }

    public Integer getCoexistVideoNumberMax() {
        return coexistVideoNumberMax;
    }

    public DockLiveCapacity setCoexistVideoNumberMax(Integer coexistVideoNumberMax) {
        this.coexistVideoNumberMax = coexistVideoNumberMax;
        return this;
    }

    public List<DockLiveCapacityDevice> getDeviceList() {
        return deviceList;
    }

    public DockLiveCapacity setDeviceList(List<DockLiveCapacityDevice> deviceList) {
        this.deviceList = deviceList;
        return this;
    }
}
