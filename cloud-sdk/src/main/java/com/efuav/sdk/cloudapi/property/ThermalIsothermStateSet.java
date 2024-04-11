package com.efuav.sdk.cloudapi.property;

import com.efuav.sdk.cloudapi.device.PayloadIndex;
import com.efuav.sdk.cloudapi.device.SwitchActionEnum;
import com.efuav.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public class ThermalIsothermStateSet extends BaseModel {

    @NotNull
    @Valid
    private PayloadIndex payloadIndex;

    @NotNull
    private SwitchActionEnum thermalIsothermState;

    public ThermalIsothermStateSet() {
    }

    @Override
    public String toString() {
        return "ThermalGainModeSet{" +
                "payloadIndex=" + payloadIndex +
                ", thermalIsothermState=" + thermalIsothermState +
                '}';
    }

    @JsonValue
    public Map<String, Object> toMap() {
        return Map.of(payloadIndex.toString(), Map.of("thermal_isotherm_state", thermalIsothermState.getAction()));
    }

    public PayloadIndex getPayloadIndex() {
        return payloadIndex;
    }

    public ThermalIsothermStateSet setPayloadIndex(PayloadIndex payloadIndex) {
        this.payloadIndex = payloadIndex;
        return this;
    }

    public SwitchActionEnum getThermalIsothermState() {
        return thermalIsothermState;
    }

    public ThermalIsothermStateSet setThermalIsothermState(SwitchActionEnum thermalIsothermState) {
        this.thermalIsothermState = thermalIsothermState;
        return this;
    }
}
