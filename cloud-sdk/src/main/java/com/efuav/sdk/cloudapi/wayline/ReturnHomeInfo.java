package com.efuav.sdk.cloudapi.wayline;

import com.efuav.sdk.cloudapi.control.Point;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public class ReturnHomeInfo {

    /**
     * 飞机的实时计划返回航线。
     * 每次推送都是对航线的完整更新。
     * 数组中有一个完整的返回路径。
     */
    private List<Point> plannedPathPoints;

    /**
     * 可以使用此字段来确定轨迹中最后一个点的显示模式。
     * 0表示轨迹的最后一个点位于地面上的返回点上方。
     * 终端可以显示将轨迹的最后一点连接到返回点的线。
     * 1表示轨迹的最后一个点不是返回点。
     * 终端不应显示将轨迹的最后一点连接到返回点的线。
     * 无法到达返回点的原因可能是返回点位于禁区内或障碍物内。
     */
    private LastPointTypeEnum lastPointType;

    /**
     * 当前工作航线任务ID
     */
    private String flightId;

    public ReturnHomeInfo() {
    }

    @Override
    public String toString() {
        return "ReturnHomeInfo{" +
                "plannedPathPoints=" + plannedPathPoints +
                ", lastPointType=" + lastPointType +
                ", flightId='" + flightId + '\'' +
                '}';
    }

    public List<Point> getPlannedPathPoints() {
        return plannedPathPoints;
    }

    public ReturnHomeInfo setPlannedPathPoints(List<Point> plannedPathPoints) {
        this.plannedPathPoints = plannedPathPoints;
        return this;
    }

    public LastPointTypeEnum getLastPointType() {
        return lastPointType;
    }

    public ReturnHomeInfo setLastPointType(LastPointTypeEnum lastPointType) {
        this.lastPointType = lastPointType;
        return this;
    }

    public String getFlightId() {
        return flightId;
    }

    public ReturnHomeInfo setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
