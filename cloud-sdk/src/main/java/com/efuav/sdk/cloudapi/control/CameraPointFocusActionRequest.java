package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class CameraPointFocusActionRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为*｛type subtype gimbalindex｝*。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private ExposureCameraTypeEnum cameraType;

    /**
     * 温度测量点的坐标x是作为坐标中心点的透镜的左上角，水平方向为x。
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float x;

    /**
     * 温度测量点的坐标y为透镜的左上角作为坐标中心点，垂直方向为y。
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float y;

    public CameraPointFocusActionRequest() {
    }

    @Override
    public String toString() {
        return "CameraPointFocusActionRequest{" +
                "payloadIndex=" + payloadIndex +
                ", cameraType=" + cameraType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public CameraPointFocusActionRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public ExposureCameraTypeEnum getCameraType() {
        return cameraType;
    }

    public CameraPointFocusActionRequest setCameraType(ExposureCameraTypeEnum cameraType) {
        this.cameraType = cameraType;
        return this;
    }

    public Float getX() {
        return x;
    }

    public CameraPointFocusActionRequest setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public CameraPointFocusActionRequest setY(Float y) {
        this.y = y;
        return this;
    }
}
