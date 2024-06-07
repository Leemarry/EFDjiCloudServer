package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/12
 */
public class CameraLookAtRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为*｛type subtype gimbalindex｝*。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

    /**
     * 无人机机头与万向节的相对位置是否锁定
     */
    @NotNull
    private Boolean locked;

    /**
     * 目标点的纬度是角度值。
     * 南纬为负值，北纬为正值。
     * 它精确到小数点后六位。
     */
    @Min(-90)
    @Max(90)
    @NotNull
    private Float latitude;

    /**
     * 目标点的纬度是角度值。
     * 负值表示西经度，正值表示东经度。
     * 它精确到小数点后六位。
     */
    @NotNull
    @Min(-180)
    @Max(180)
    private Float longitude;

    /**
     * 椭球体高度
     */
    @NotNull
    @Min(2)
    @Max(10000)
    private Float height;

    public CameraLookAtRequest() {
    }

    @Override
    public String toString() {
        return "CameraLookAtRequest{" +
                "payloadIndex=" + payloadIndex +
                ", locked=" + locked +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                '}';
    }

    public CameraLookAtRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public CameraLookAtRequest setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public CameraLookAtRequest setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public CameraLookAtRequest setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public CameraLookAtRequest setHeight(Float height) {
        this.height = height;
        return this;
    }
}
