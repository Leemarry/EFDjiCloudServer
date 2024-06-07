package com.efuav.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/19
 */
public class DongleInfo {

    /**
     * 加密狗独有的识别标志
     */
    private String imei;

    /**
     * 加密狗类型
     */
    private DongleTypeEnum dongleType;

    /**
     * esim的唯一标识用于公众账号查询包和购买服务。
     */
    private String eid;

    /**
     * esim激活状态
     */
    private EsimActivateStateEnum esimActivateState;

    /**
     * 加密狗中物理sim卡的状态。
     */
    private SimCardStateEnum simCardState;

    /**
     * 标识加密狗当前正在使用的sim卡插槽。
     */
    private SimSlotEnum simSlot;

    /**
     * esim信息
     */
    private List<EsimInfo> esimInfos;

    /**
     * 可以插入加密狗的物理sim卡信息。
     */
    private SimInfo simInfo;

    public DongleInfo() {
    }

    @Override
    public String toString() {
        return "DongleInfo{" +
                "imei='" + imei + '\'' +
                ", dongleType=" + dongleType +
                ", eid='" + eid + '\'' +
                ", esimActivateState=" + esimActivateState +
                ", simCardState=" + simCardState +
                ", simSlot=" + simSlot +
                ", esimInfos=" + esimInfos +
                ", simInfo=" + simInfo +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public DongleInfo setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public DongleTypeEnum getDongleType() {
        return dongleType;
    }

    public DongleInfo setDongleType(DongleTypeEnum dongleType) {
        this.dongleType = dongleType;
        return this;
    }

    public String getEid() {
        return eid;
    }

    public DongleInfo setEid(String eid) {
        this.eid = eid;
        return this;
    }

    public EsimActivateStateEnum getEsimActivateState() {
        return esimActivateState;
    }

    public DongleInfo setEsimActivateState(EsimActivateStateEnum esimActivateState) {
        this.esimActivateState = esimActivateState;
        return this;
    }

    public SimCardStateEnum getSimCardState() {
        return simCardState;
    }

    public DongleInfo setSimCardState(SimCardStateEnum simCardState) {
        this.simCardState = simCardState;
        return this;
    }

    public SimSlotEnum getSimSlot() {
        return simSlot;
    }

    public DongleInfo setSimSlot(SimSlotEnum simSlot) {
        this.simSlot = simSlot;
        return this;
    }

    public List<EsimInfo> getEsimInfos() {
        return esimInfos;
    }

    public DongleInfo setEsimInfos(List<EsimInfo> esimInfos) {
        this.esimInfos = esimInfos;
        return this;
    }

    public SimInfo getSimInfo() {
        return simInfo;
    }

    public DongleInfo setSimInfo(SimInfo simInfo) {
        this.simInfo = simInfo;
        return this;
    }
}
