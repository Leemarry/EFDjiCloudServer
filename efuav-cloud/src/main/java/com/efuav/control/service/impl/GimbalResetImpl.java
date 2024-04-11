package com.efuav.control.service.impl;

import com.efuav.control.model.param.DronePayloadParam;

import java.util.Objects;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public class GimbalResetImpl extends PayloadCommandsHandler {
    public GimbalResetImpl(DronePayloadParam param) {
        super(param);
    }

    @Override
    public boolean valid() {
        return Objects.nonNull(param.getResetMode());
    }

}
