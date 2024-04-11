package com.efuav.component;

import com.efuav.component.redis.RedisConst;
import com.efuav.component.redis.RedisOpsUtils;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.manage.service.IDeviceService;
import com.efuav.sdk.cloudapi.device.DeviceDomainEnum;
import com.efuav.sdk.common.SDKManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
@Component
public class ApplicationBootInitial implements CommandLineRunner {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    /**
     * 当程序启动时，订阅redis中存在的设备，
     * 以防止数据由于节目中断而与导频侧不同。
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        int start = RedisConst.DEVICE_ONLINE_PREFIX.length();

        RedisOpsUtils.getAllKeys(RedisConst.DEVICE_ONLINE_PREFIX + "*")
                .stream()
                .map(key -> key.substring(start))
                .map(deviceRedisService::getDeviceOnline)
                .map(Optional::get)
                .filter(device -> DeviceDomainEnum.DRONE != device.getDomain())
                .forEach(device -> deviceService.subDeviceOnlineSubscribeTopic(
                        SDKManager.registerDevice(device.getDeviceSn(), device.getChildDeviceSn(), device.getDomain(),
                                device.getType(), device.getSubType(), device.getThingVersion(),
                                deviceRedisService.getDeviceOnline(device.getChildDeviceSn()).map(DeviceDTO::getThingVersion).orElse(null))));

    }
}