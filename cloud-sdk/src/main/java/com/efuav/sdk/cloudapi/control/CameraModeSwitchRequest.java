package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.CameraModeEnum;
import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public class CameraModeSwitchRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private CameraModeEnum cameraMode;

    public CameraModeSwitchRequest() {
    }

    @Override
    public String toString() {
        return "CameraModeSwitchRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraMode=" + cameraMode +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraModeSwitchRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public CameraModeEnum getCameraMode() {
        return cameraMode;
    }

    public CameraModeSwitchRequest setCameraMode(CameraModeEnum cameraMode) {
        this.cameraMode = cameraMode;
        return this;
    }
}
