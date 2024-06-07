package com.efuav.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class SimInfo {

    /**
     * sim支持的运营商
     */
    private TelecomOperatorEnum telecomOperator;

    /**
     * 物理sim卡类型
     */
    private SimTypeEnum simType;

    /**
     * sim卡的唯一识别标记可用于购买实体sim卡包。
     */
    private String iccid;

    public SimInfo() {
    }

    @Override
    public String toString() {
        return "SimInfo{" +
                "telecomOperator=" + telecomOperator +
                ", simType=" + simType +
                ", iccid='" + iccid + '\'' +
                '}';
    }

    public TelecomOperatorEnum getTelecomOperator() {
        return telecomOperator;
    }

    public SimInfo setTelecomOperator(TelecomOperatorEnum telecomOperator) {
        this.telecomOperator = telecomOperator;
        return this;
    }

    public SimTypeEnum getSimType() {
        return simType;
    }

    public SimInfo setSimType(SimTypeEnum simType) {
        this.simType = simType;
        return this;
    }

    public String getIccid() {
        return iccid;
    }

    public SimInfo setIccid(String iccid) {
        this.iccid = iccid;
        return this;
    }
}
