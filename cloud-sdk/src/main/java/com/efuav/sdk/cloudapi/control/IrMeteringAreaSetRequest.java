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
public class IrMeteringAreaSetRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为*｛type subtype gimbalindex｝*。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

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

    /**
     * 温度测量区域宽度
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float width;

    /**
     * 测温区域高度
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Float height;

    public IrMeteringAreaSetRequest() {
    }

    @Override
    public String toString() {
        return "IrMeteringAreaSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public IrMeteringAreaSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public Float getX() {
        return x;
    }

    public IrMeteringAreaSetRequest setX(Float x) {
        this.x = x;
        return this;
    }

    public Float getY() {
        return y;
    }

    public IrMeteringAreaSetRequest setY(Float y) {
        this.y = y;
        return this;
    }

    public Float getWidth() {
        return width;
    }

    public IrMeteringAreaSetRequest setWidth(Float width) {
        this.width = width;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public IrMeteringAreaSetRequest setHeight(Float height) {
        this.height = height;
        return this;
    }
}
