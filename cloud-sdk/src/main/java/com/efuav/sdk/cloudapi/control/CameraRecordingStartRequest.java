package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public class CameraRecordingStartRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    public CameraRecordingStartRequest() {
    }

    @Override
    public String toString() {
        return "CameraRecordingStartRequest{" +
                "payloadIndex=" + payloadIndex +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraRecordingStartRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }
}
