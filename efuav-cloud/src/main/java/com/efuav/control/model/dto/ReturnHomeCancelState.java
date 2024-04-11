package com.efuav.control.model.dto;

import com.efuav.common.util.SpringBeanUtilsTest;
import com.efuav.control.service.impl.RemoteDebugHandler;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.sdk.cloudapi.device.DroneModeCodeEnum;
import com.efuav.sdk.cloudapi.device.OsdDockDrone;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/19
 */

public class ReturnHomeCancelState extends RemoteDebugHandler {

    @Override
    public boolean canPublish(String sn) {
        IDeviceRedisService deviceRedisService = SpringBeanUtilsTest.getBean(IDeviceRedisService.class);
        return deviceRedisService.getDeviceOnline(sn)
                .map(DeviceDTO::getChildDeviceSn)
                .flatMap(deviceSn -> deviceRedisService.getDeviceOsd(deviceSn, OsdDockDrone.class))
                .map(osd -> DroneModeCodeEnum.RETURN_AUTO == osd.getModeCode())
                .orElse(false);
    }

}
