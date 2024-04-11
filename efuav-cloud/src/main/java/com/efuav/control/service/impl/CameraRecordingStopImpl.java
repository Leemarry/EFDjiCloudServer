package com.efuav.control.service.impl;

import com.efuav.control.model.param.DronePayloadParam;
import com.efuav.sdk.cloudapi.device.CameraStateEnum;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public class CameraRecordingStopImpl extends PayloadCommandsHandler {

    public CameraRecordingStopImpl(DronePayloadParam param) {
        super(param);
    }

    @Override
    public boolean canPublish(String deviceSn) {
        super.canPublish(deviceSn);
        return CameraStateEnum.WORKING == osdCamera.getRecordingState();
    }
}
