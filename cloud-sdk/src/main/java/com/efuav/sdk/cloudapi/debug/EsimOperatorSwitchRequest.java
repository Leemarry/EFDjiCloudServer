package com.efuav.sdk.cloudapi.debug;

import com.efuav.sdk.cloudapi.device.TelecomOperatorEnum;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class EsimOperatorSwitchRequest extends BaseModel {

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
     * 切换的目标载波。
     */
    @NotNull
    private TelecomOperatorEnum telecomOperator;

    public EsimOperatorSwitchRequest() {
    }

    @Override
    public String toString() {
        return "EsimOperatorSwitchRequest{" +
                "imei='" + imei + '\'' +
                ", deviceType=" + deviceType +
                ", telecomOperator=" + telecomOperator +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public EsimOperatorSwitchRequest setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public DongleDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public EsimOperatorSwitchRequest setDeviceType(DongleDeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public TelecomOperatorEnum getTelecomOperator() {
        return telecomOperator;
    }

    public EsimOperatorSwitchRequest setTelecomOperator(TelecomOperatorEnum telecomOperator) {
        this.telecomOperator = telecomOperator;
        return this;
    }
}
