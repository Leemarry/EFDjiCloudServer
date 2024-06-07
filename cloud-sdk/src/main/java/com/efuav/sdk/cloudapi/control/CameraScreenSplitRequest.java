package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/12
 */
public class CameraScreenSplitRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为*｛type subtype gimbalindex｝*。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

    /**
     * 是否启用分屏功能
     */
    @NotNull
    private Boolean enable;

    public CameraScreenSplitRequest() {
    }

    @Override
    public String toString() {
        return "CameraScreenSplitRequest{" +
                "payloadIndex=" + payloadIndex +
                ", enable=" + enable +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraScreenSplitRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public Boolean getEnable() {
        return enable;
    }

    public CameraScreenSplitRequest setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }
}
