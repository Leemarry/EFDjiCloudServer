package com.efuav.sdk.cloudapi.livestream;

import com.efuav.sdk.cloudapi.device.VideoId;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class LiveSetQualityRequest extends BaseModel {

    /**
     * 格式为#{uav_sn}/#{camera_id}/#{video_index}，
     * 无人机序列号/有效载荷和安装位置枚举值/有效载荷镜头编号
     */
    @NotNull
    private VideoId videoId;

    @NotNull
    private VideoQualityEnum videoQuality;

    public LiveSetQualityRequest() {
    }

    @Override
    public String toString() {
        return "LiveSetQualityRequest{" +
                "videoId=" + videoId +
                ", videoQuality=" + videoQuality +
                '}';
    }

    public VideoId getVideoId() {
        return videoId;
    }

    public LiveSetQualityRequest setVideoId(VideoId videoId) {
        this.videoId = videoId;
        return this;
    }

    public VideoQualityEnum getVideoQuality() {
        return videoQuality;
    }

    public LiveSetQualityRequest setVideoQuality(VideoQualityEnum videoQuality) {
        this.videoQuality = videoQuality;
        return this;
    }
}
