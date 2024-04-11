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
     * The format is #{uav_sn}/#{camera_id}/#{video_index},
     * drone serial number/payload and mounted location enumeration value/payload lens numbering
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
