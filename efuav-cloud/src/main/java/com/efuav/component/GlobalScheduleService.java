package com.efuav.component;

import com.efuav.component.redis.RedisConst;
import com.efuav.component.redis.RedisOpsUtils;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.service.IDeviceService;
import com.efuav.sdk.cloudapi.device.DeviceDomainEnum;
import com.efuav.sdk.mqtt.IMqttTopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author sean.zhou
 * @date 2021/11/24
 * @version 0.1
 */
@Component
@Slf4j
public class GlobalScheduleService {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IMqttTopicService topicService;

    @Autowired
    private ObjectMapper mapper;
    /**
     * 每30秒检查一次设备的状态。建议使用缓存。
     */
    @Scheduled(initialDelay = 10, fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    private void deviceStatusListen() {
        String deviceOnlinePrefix = RedisConst.DEVICE_ONLINE_PREFIX + "*";

        // 获取所有设备状态信息的键
        Set<String> deviceKeys = RedisOpsUtils.getAllKeys(deviceOnlinePrefix);
        for (String key : deviceKeys) {
            // 获取设备状态信息
            DeviceDTO device = (DeviceDTO) RedisOpsUtils.get(key);
            if (device != null) {
                long expire = RedisOpsUtils.getExpire(key);
                if (expire <= 30) {
                    // 设备状态变更，进行相应处理
                    if (DeviceDomainEnum.DRONE == device.getDomain()) {
                        deviceService.subDeviceOffline(key.substring(RedisConst.DEVICE_ONLINE_PREFIX.length()));
                    } else {
                        deviceService.gatewayOffline(key.substring(RedisConst.DEVICE_ONLINE_PREFIX.length()));
                    }
                    // 删除缓存中的设备状态信息
                    RedisOpsUtils.del(key);
                }
            }
        }

        log.info("订阅: {}", Arrays.toString(topicService.getSubscribedTopic()));
    }

}