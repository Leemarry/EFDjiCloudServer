package com.efuav.sdk.cloudapi.property;

import com.efuav.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class DistanceLimitStatusSet extends BaseModel {

    @Valid
    @NotNull
    private DistanceLimitStatusData distanceLimitStatus;

    public DistanceLimitStatusSet() {
    }

    @Override
    public String toString() {
        return "DistanceLimitStatusSet{" +
                "distanceLimitStatus=" + distanceLimitStatus +
                '}';
    }

    public DistanceLimitStatusData getDistanceLimitStatus() {
        return distanceLimitStatus;
    }

    public DistanceLimitStatusSet setDistanceLimitStatus(DistanceLimitStatusData distanceLimitStatus) {
        this.distanceLimitStatus = distanceLimitStatus;
        return this;
    }
}
