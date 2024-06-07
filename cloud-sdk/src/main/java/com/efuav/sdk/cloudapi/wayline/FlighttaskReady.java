package com.efuav.sdk.cloudapi.wayline;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskReady {

    /**
     * 当前满足任务准备条件的任务ID集
     */
    private List<String> flightIds;

    public FlighttaskReady() {
    }

    @Override
    public String toString() {
        return "FlighttaskReady{" +
                "flightIds=" + flightIds +
                '}';
    }

    public List<String> getFlightIds() {
        return flightIds;
    }

    public FlighttaskReady setFlightIds(List<String> flightIds) {
        this.flightIds = flightIds;
        return this;
    }
}
