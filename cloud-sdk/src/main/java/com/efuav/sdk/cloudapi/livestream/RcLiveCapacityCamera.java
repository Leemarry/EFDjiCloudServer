package com.efuav.sdk.cloudapi.livestream;

import com.efuav.sdk.cloudapi.device.PayloadIndex;

import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/18
 */
public class RcLiveCapacityCamera {

    /**
     * 可用于直播的视频流总数
     * 摄像机可以直播的视频流总数
     */
    private Integer availableVideoNumber;

    /**
     * 摄像机可以同时直播的最大视频流数。
     */
    private Integer coexistVideoNumberMax;

    /**
     * 相机索引，由产品类型枚举和万向节索引组成。
     */
    private PayloadIndex cameraIndex;

    private List<RcLiveCapacityVideo> videoList;

    public RcLiveCapacityCamera() {
    }

    @Override
    public String toString() {
        return "RcLiveCapacityCamera{" +
                "availableVideoNumber=" + availableVideoNumber +
                ", coexistVideoNumberMax=" + coexistVideoNumberMax +
                ", cameraIndex=" + cameraIndex +
                ", videoList=" + videoList +
                '}';
    }

    public Integer getAvailableVideoNumber() {
        return availableVideoNumber;
    }

    public RcLiveCapacityCamera setAvailableVideoNumber(Integer availableVideoNumber) {
        this.availableVideoNumber = availableVideoNumber;
        return this;
    }

    public Integer getCoexistVideoNumberMax() {
        return coexistVideoNumberMax;
    }

    public RcLiveCapacityCamera setCoexistVideoNumberMax(Integer coexistVideoNumberMax) {
        this.coexistVideoNumberMax = coexistVideoNumberMax;
        return this;
    }

    public PayloadIndex getCameraIndex() {
        return cameraIndex;
    }

    public RcLiveCapacityCamera setCameraIndex(PayloadIndex cameraIndex) {
        this.cameraIndex = cameraIndex;
        return this;
    }

    public List<RcLiveCapacityVideo> getVideoList() {
        return videoList;
    }

    public RcLiveCapacityCamera setVideoList(List<RcLiveCapacityVideo> videoList) {
        this.videoList = videoList;
        return this;
    }
}