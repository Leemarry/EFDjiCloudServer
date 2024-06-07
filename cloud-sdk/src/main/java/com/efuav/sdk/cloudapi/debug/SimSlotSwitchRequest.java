package com.efuav.sdk.cloudapi.debug;

import com.efuav.sdk.cloudapi.device.SimSlotEnum;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class SimSlotSwitchRequest extends BaseModel {

    /**
     * 标识要操作的加密狗。
     */
    @NotNull
    private String imei;

    /**
     * 标识要操作的目标设备。
     */
    @NotNull
    private DongleDeviceTypeEnum deviceType;

    /**
     * 在使用物理sim卡和使用esim之间切换。
     */
    @NotNull
    private SimSlotEnum simSlot;

    public SimSlotSwitchRequest() {
    }

    @Override
    public String toString() {
        return "SimSlotSwitchRequest{" +
                "imei='" + imei + '\'' +
                ", deviceType=" + deviceType +
                ", simSlot=" + simSlot +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public SimSlotSwitchRequest setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public DongleDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public SimSlotSwitchRequest setDeviceType(DongleDeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public SimSlotEnum getSimSlot() {
        return simSlot;
    }

    public SimSlotSwitchRequest setSimSlot(SimSlotEnum simSlot) {
        this.simSlot = simSlot;
        return this;
    }
}
