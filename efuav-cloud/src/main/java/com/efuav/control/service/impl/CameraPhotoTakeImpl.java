package com.efuav.control.service.impl;

import com.efuav.control.model.param.DronePayloadParam;
import com.efuav.sdk.cloudapi.device.CameraStateEnum;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public class CameraPhotoTakeImpl extends PayloadCommandsHandler {

    public CameraPhotoTakeImpl(DronePayloadParam param) {
        super(param);
    }

    @Override
    public boolean canPublish(String deviceSn) {
        super.canPublish(deviceSn);
        return CameraStateEnum.WORKING != osdCamera.getPhotoState() && osdCamera.getRemainPhotoNum() > 0;
    }
}
