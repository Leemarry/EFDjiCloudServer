package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class CameraPhotoStopRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    public CameraPhotoStopRequest() {
    }

    @Override
    public String toString() {
        return "CameraPhotoStopRequest{" +
                "payloadIndex=" + payloadIndex +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraPhotoStopRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }
}
