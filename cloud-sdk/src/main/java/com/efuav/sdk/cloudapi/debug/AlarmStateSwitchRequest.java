package com.efuav.sdk.cloudapi.debug;

import com.efuav.sdk.cloudapi.device.SwitchActionEnum;
import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public class AlarmStateSwitchRequest extends BaseModel {

    @NotNull
    private SwitchActionEnum action;

    public AlarmStateSwitchRequest() {
    }

    @Override
    public String toString() {
        return "AlarmStateSwitchRequest{" +
                "action=" + action +
                '}';
    }

    public SwitchActionEnum getAction() {
        return action;
    }

    public AlarmStateSwitchRequest setAction(SwitchActionEnum action) {
        this.action = action;
        return this;
    }
}
