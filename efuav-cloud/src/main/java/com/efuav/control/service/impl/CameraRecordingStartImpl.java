package com.efuav.control.service.impl;

import com.efuav.control.model.param.DronePayloadParam;
import com.efuav.sdk.cloudapi.device.CameraModeEnum;
import com.efuav.sdk.cloudapi.device.CameraStateEnum;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public class CameraRecordingStartImpl extends PayloadCommandsHandler {

    public CameraRecordingStartImpl(DronePayloadParam param) {
        super(param);
    }

    @Override
    public boolean canPublish(String deviceSn) {
        super.canPublish(deviceSn);
        return CameraModeEnum.VIDEO == osdCamera.getCameraMode()
                && CameraStateEnum.IDLE == osdCamera.getRecordingState()
                && osdCamera.getRemainRecordDuration() > 0;
    }
}
