package com.efuav.sdk.cloudapi.livestream;

import com.efuav.sdk.cloudapi.device.VideoId;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class LiveStopPushRequest extends BaseModel {

    /**
     * 格式为#{uav_sn}/#{camera_id}/#{video_index}，
     * 无人机序列号/有效载荷和安装位置枚举值/有效载荷镜头编号
     */
    @NotNull
    private VideoId videoId;

    public LiveStopPushRequest() {
    }

    @Override
    public String toString() {
        return "LiveStopPushRequest{" +
                "videoId=" + videoId +
                '}';
    }

    public VideoId getVideoId() {
        return videoId;
    }

    public LiveStopPushRequest setVideoId(VideoId videoId) {
        this.videoId = videoId;
        return this;
    }
}
