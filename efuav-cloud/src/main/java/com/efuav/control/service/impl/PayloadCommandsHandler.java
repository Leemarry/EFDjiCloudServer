package com.efuav.control.service.impl;

import com.efuav.common.util.SpringBeanUtilsTest;
import com.efuav.control.model.param.DronePayloadParam;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.service.IDevicePayloadService;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.sdk.cloudapi.device.OsdCamera;
import com.efuav.sdk.cloudapi.device.OsdDockDrone;

import java.util.Optional;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public abstract class PayloadCommandsHandler {

    DronePayloadParam param;

    OsdCamera osdCamera;

    PayloadCommandsHandler(DronePayloadParam param) {
        this.param = param;
    }

    public boolean valid() {
        return true;
    }

    public boolean canPublish(String deviceSn) {
        Optional<OsdDockDrone> deviceOpt = SpringBeanUtilsTest.getBean(IDeviceRedisService.class)
                .getDeviceOsd(deviceSn, OsdDockDrone.class);
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("设备处于离线状态。");
        }
        osdCamera = deviceOpt.get().getCameras().stream()
                .filter(osdCamera -> param.getPayloadIndex().equals(osdCamera.getPayloadIndex().toString()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("没有收到关于相机的osd信息，请检查缓存数据。"));
        return true;
    }

    private String checkDockOnline(String dockSn) {
        Optional<DeviceDTO> deviceOpt = SpringBeanUtilsTest.getBean(IDeviceRedisService.class).getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("机场处于离线状态。");
        }
        return deviceOpt.get().getChildDeviceSn();
    }

    private void checkDeviceOnline(String deviceSn) {
        boolean isOnline = SpringBeanUtilsTest.getBean(IDeviceRedisService.class).checkDeviceOnline(deviceSn);
        if (!isOnline) {
            throw new RuntimeException("设备处于离线状态。");
        }
    }

    private void checkAuthority(String deviceSn) {
        boolean hasAuthority = SpringBeanUtilsTest.getBean(IDevicePayloadService.class)
                .checkAuthorityPayload(deviceSn, param.getPayloadIndex());
        if (!hasAuthority) {
            throw new RuntimeException("该设备没有有效负载控制权限。");
        }
    }

    public final void checkCondition(String dockSn) {
        if (!valid()) {
            throw new RuntimeException("非法参数");
        }

        String deviceSn = checkDockOnline(dockSn);
        checkDeviceOnline(deviceSn);
        checkAuthority(deviceSn);

        if (!canPublish(deviceSn)) {
            throw new RuntimeException("无人机的当前状态不支持此功能，请稍后再试。");
        }
    }

}
