package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class CameraExposureSetRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为｛type subtype gimbalindex｝。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private ExposureCameraTypeEnum cameraType;

    @NotNull
    private ExposureValueEnum exposureValue;

    public CameraExposureSetRequest() {
    }

    @Override
    public String toString() {
        return "CameraExposureSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraType=" + cameraType +
                ", exposureValue=" + exposureValue +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraExposureSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ExposureCameraTypeEnum getCameraType() {
        return cameraType;
    }

    public CameraExposureSetRequest setCameraType(ExposureCameraTypeEnum cameraType) {
        this.cameraType = cameraType;
        return this;
    }

    public ExposureValueEnum getExposureValue() {
        return exposureValue;
    }

    public CameraExposureSetRequest setExposureValue(ExposureValueEnum exposureValue) {
        this.exposureValue = exposureValue;
        return this;
    }
}
