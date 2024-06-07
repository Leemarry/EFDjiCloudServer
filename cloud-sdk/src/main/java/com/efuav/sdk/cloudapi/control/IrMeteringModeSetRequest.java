package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class IrMeteringModeSetRequest extends BaseModel {

    /**
     * 摄像机枚举。
     * 它是非官方的device_mode_key。
     * 格式为*｛type subtype gimbalindex｝*。
     * 请阅读[支持的产品]（https://developer.di.com/doc/cloud-api-tutorial/en/overview/Product-support.html）
     */
    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private MeteringModeEnum mode;

    public IrMeteringModeSetRequest() {
    }

    @Override
    public String toString() {
        return "IrMeteringModeSetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", mode=" + mode +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public IrMeteringModeSetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public MeteringModeEnum getMode() {
        return mode;
    }

    public IrMeteringModeSetRequest setMode(MeteringModeEnum mode) {
        this.mode = mode;
        return this;
    }
}
