package com.efuav.sdk.cloudapi.control;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public class GimbalResetRequest extends BaseModel {

    @NotNull
    private PayloadIndex payloadIndex;

    @NotNull
    private GimbalResetModeEnum resetMode;

    public GimbalResetRequest() {
    }

    @Override
    public String toString() {
        return "GimbalResetRequest{" +
                "payloadIndex=" + payloadIndex +
                ", resetMode=" + resetMode +
                '}';
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public GimbalResetRequest setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public GimbalResetModeEnum getResetMode() {
        return resetMode;
    }

    public GimbalResetRequest setResetMode(GimbalResetModeEnum resetMode) {
        this.resetMode = resetMode;
        return this;
    }
}
