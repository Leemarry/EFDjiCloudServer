package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/12
 */
public class VideoStorageSetRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为*｛type subtype gimbalindex｝*。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

    /**
     * 视频存储类型。多选。
     */
    @NotNull
    @Size(min = 1)
    private List<LensStorageSettingsEnum> videoStorageSettings;

    public VideoStorageSetRequest() {
    }

    @Override
    public String toString() {
        return "VideoStorageSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", videoStorageSettings=" + videoStorageSettings +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public VideoStorageSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public List<LensStorageSettingsEnum> getVideoStorageSettings() {
        return videoStorageSettings;
    }

    public VideoStorageSetRequest setVideoStorageSettings(List<LensStorageSettingsEnum> videoStorageSettings) {
        this.videoStorageSettings = videoStorageSettings;
        return this;
    }
}
