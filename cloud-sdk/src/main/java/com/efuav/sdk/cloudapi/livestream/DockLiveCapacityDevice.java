package com.efuav.sdk.cloudapi.livestream;

import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/18
 */
public class DockLiveCapacityDevice {

    /**
     * 设备序列号
     */
    private String sn;

    /**
     * 可用于直播的视频流总数
     * 用于属于设备的直播的视频流总数。
     */
    private Integer availableVideoNumber;

    /**
     * 可同时用于直播的最大视频流数量
     */
    private Integer coexistVideoNumberMax;

    /**
     * 设备上的摄像头列表
     */
    private List<DockLiveCapacityCamera> cameraList;

    public DockLiveCapacityDevice() {
    }

    @Override
    public String toString() {
        return "DockLiveCapacityDevice{" +
                "sn='" + sn + '\'' +
                ", availableVideoNumber=" + availableVideoNumber +
                ", coexistVideoNumberMax=" + coexistVideoNumberMax +
                ", cameraList=" + cameraList +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public DockLiveCapacityDevice setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public Integer getAvailableVideoNumber() {
        return availableVideoNumber;
    }

    public DockLiveCapacityDevice setAvailableVideoNumber(Integer availableVideoNumber) {
        this.availableVideoNumber = availableVideoNumber;
        return this;
    }

    public Integer getCoexistVideoNumberMax() {
        return coexistVideoNumberMax;
    }

    public DockLiveCapacityDevice setCoexistVideoNumberMax(Integer coexistVideoNumberMax) {
        this.coexistVideoNumberMax = coexistVideoNumberMax;
        return this;
    }

    public List<DockLiveCapacityCamera> getCameraList() {
        return cameraList;
    }

    public DockLiveCapacityDevice setCameraList(List<DockLiveCapacityCamera> cameraList) {
        this.cameraList = cameraList;
        return this;
    }
}