package com.efuav.manage.service;

import com.efuav.component.mqtt.model.EventsReceiver;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.sdk.cloudapi.firmware.OtaProgress;

import java.util.Optional;
import java.util.Set;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/21
 */
public interface IDeviceRedisService {

    /**
     * 确定设备是否在线。
     * @param sn
     * @return
     */
    Boolean checkDeviceOnline(String sn);

    /**
     * 在redis中查询设备的基本信息。
     * @param sn
     * @return
     */
    Optional<DeviceDTO> getDeviceOnline(String sn);

    /**
     * 将设备的基本信息保存在redis中。
     * @param device
     */
    void setDeviceOnline(DeviceDTO device);

    /**
     * 删除redis中保存的基本设备信息。
     * @param sn
     * @return
     */
    Boolean delDeviceOnline(String sn);

    /**
     * 保存设备的osd实时数据。
     * @param sn
     * @param data
     * @return
     */
    void setDeviceOsd(String sn, Object data);

    /**
     * 获取设备的osd实时数据。
     * @param sn
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Optional<T> getDeviceOsd(String sn, Class<T> clazz);
    /**
     * 删除设备的osd实时数据。
     * @param sn
     * @return
     */
    Boolean delDeviceOsd(String sn);

    /**
     * 将设备的固件更新进度保存在redis中。
     * @param sn
     * @param events
     */
    void setFirmwareUpgrading(String sn, EventsReceiver<OtaProgress> events);

    /**
     * 在redis中查询设备的固件更新进度。
     * @param sn
     * @return
     */
    Optional<EventsReceiver<OtaProgress>> getFirmwareUpgradingProgress(String sn);

    /**
     * 在redis中删除设备的固件更新进度。
     * @param sn
     * @return
     */
    Boolean delFirmwareUpgrading(String sn);

    /**
     * 将设备的hms密钥保存在redis中。
     * @param sn
     * @param keys
     */
    void addEndHmsKeys(String sn, String... keys);

    /**
     * 在redis中查询设备的所有hms密钥。
     * @param sn
     * @return
     */
    Set<String> getAllHmsKeys(String sn);

    /**
     * 删除redis中设备的所有hms密钥。
     * @param sn
     * @return
     */
    Boolean delHmsKeysBySn(String sn);

    void gatewayOffline(String gatewaySn);

    void subDeviceOffline(String deviceSn);
}
