package com.efuav.sdk.cloudapi.livestream;

import com.efuav.sdk.cloudapi.device.VideoId;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class LiveLensChangeRequest extends BaseModel {

    @NotNull
    private LensChangeVideoTypeEnum videoType;

    /**
     * 格式为#{uav_sn}/#{camera_id}/#{video_index}，
     * 无人机序列号/有效载荷和安装位置枚举值/有效载荷镜头编号
     */
    @NotNull
    private VideoId videoId;

    public LiveLensChangeRequest() {
    }

    @Override
    public String toString() {
        return "LiveLensChangeRequest{" +
                "videoType=" + videoType +
                ", videoId=" + videoId +
                '}';
    }

    public LensChangeVideoTypeEnum getVideoType() {
        return videoType;
    }

    public LiveLensChangeRequest setVideoType(LensChangeVideoTypeEnum videoType) {
        this.videoType = videoType;
        return this;
    }

    public VideoId getVideoId() {
        return videoId;
    }

    public LiveLensChangeRequest setVideoId(VideoId videoId) {
        this.videoId = videoId;
        return this;
    }
}
