package com.efuav.sdk.cloudapi.device;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class EsimInfo {

    /**
     * esim支持的运算符
     */
    private TelecomOperatorEnum telecomOperator;

    /**
     * 在esim-info中，只能同时启用一个esim。
     */
    private Boolean enabled;

    /**
     * sim卡的唯一识别标记可用于购买实体sim卡包。
     */
    private String iccid;

    public EsimInfo() {
    }

    @Override
    public String toString() {
        return "EsimInfo{" +
                "telecomOperator=" + telecomOperator +
                ", enabled=" + enabled +
                ", iccid='" + iccid + '\'' +
                '}';
    }

    public TelecomOperatorEnum getTelecomOperator() {
        return telecomOperator;
    }

    public EsimInfo setTelecomOperator(TelecomOperatorEnum telecomOperator) {
        this.telecomOperator = telecomOperator;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public EsimInfo setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getIccid() {
        return iccid;
    }

    public EsimInfo setIccid(String iccid) {
        this.iccid = iccid;
        return this;
    }
}
