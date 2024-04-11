package com.efuav.control.service.impl;

import com.efuav.common.util.SpringBeanUtilsTest;
import com.efuav.manage.service.IDeviceService;
import com.efuav.sdk.cloudapi.device.DockModeCodeEnum;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class RemoteDebugHandler {

    public boolean valid() {
        return true;
    }

    public boolean canPublish(String sn) {
        IDeviceService deviceService = SpringBeanUtilsTest.getBean(IDeviceService.class);
        DockModeCodeEnum dockMode = deviceService.getDockMode(sn);
        return DockModeCodeEnum.REMOTE_DEBUGGING == dockMode;
    }
}
