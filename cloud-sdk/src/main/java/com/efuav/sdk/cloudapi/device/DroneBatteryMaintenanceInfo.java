package com.efuav.sdk.cloudapi.device;

import java.util.List;

/**
 * @author sean
 * @version 1.4
 * @date 2022/11/3
 */
public class DroneBatteryMaintenanceInfo {

    private List<DroneBatteryMaintenance> batteries;

    private MaintenanceStateEnum maintenanceState;

    /**
     * 电池维护剩余时间
     * 四舍五入
     * 单位：小时
     */
    private Integer maintenanceTimeLeft;

    /**
     * 当无人机在机场断电时，将报告无人机电池的加热和绝缘信息。
     */
    private HeatStateEnum heatState;

    public DroneBatteryMaintenanceInfo() {
    }

    @Override
    public String toString() {
        return "DroneBatteryMaintenanceInfo{" +
                "batteries=" + batteries +
                ", maintenanceState=" + maintenanceState +
                ", maintenanceTimeLeft=" + maintenanceTimeLeft +
                ", heatState=" + heatState +
                '}';
    }

    public List<DroneBatteryMaintenance> getBatteries() {
        return batteries;
    }

    public DroneBatteryMaintenanceInfo setBatteries(List<DroneBatteryMaintenance> batteries) {
        this.batteries = batteries;
        return this;
    }

    public MaintenanceStateEnum getMaintenanceState() {
        return maintenanceState;
    }

    public DroneBatteryMaintenanceInfo setMaintenanceState(MaintenanceStateEnum maintenanceState) {
        this.maintenanceState = maintenanceState;
        return this;
    }

    public Integer getMaintenanceTimeLeft() {
        return maintenanceTimeLeft;
    }

    public DroneBatteryMaintenanceInfo setMaintenanceTimeLeft(Integer maintenanceTimeLeft) {
        this.maintenanceTimeLeft = maintenanceTimeLeft;
        return this;
    }

    public HeatStateEnum getHeatState() {
        return heatState;
    }

    public DroneBatteryMaintenanceInfo setHeatState(HeatStateEnum heatState) {
        this.heatState = heatState;
        return this;
    }
}
