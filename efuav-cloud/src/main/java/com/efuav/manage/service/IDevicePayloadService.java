package com.efuav.manage.service;

import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.model.dto.DevicePayloadDTO;
import com.efuav.manage.model.dto.DevicePayloadReceiver;
import com.efuav.sdk.cloudapi.device.PayloadFirmwareVersion;

import java.util.Collection;
import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface IDevicePayloadService {

    /**
     * 根据有效载荷的sn查询有效载荷是否已保存。
     * @param payloadSn
     * @return
     */
    Integer checkPayloadExist(String payloadSn);

    /**
     * 保存所有有效载荷数据。
     * @param payloadReceiverList
     * @return
     */
    Boolean savePayloadDTOs(DeviceDTO drone, List<DevicePayloadReceiver> payloadReceiverList);

    /**
     * 保存有效载荷数据。
     * @param payloadReceiver
     * @return
     */
    Integer saveOnePayloadDTO(DevicePayloadReceiver payloadReceiver);

    /**
     * 基于设备sn查询该设备上的所有有效载荷数据。
     * @param deviceSn
     * @return
     */
    List<DevicePayloadDTO> getDevicePayloadEntitiesByDeviceSn(String deviceSn);

    /**
     * 根据设备sns的集合删除这些设备上的所有有效载荷数据。
     * @param deviceSns
     */
    void deletePayloadsByDeviceSn(List<String> deviceSns);

    /**
     * 更新有效负载的固件版本信息。
     * @param receiver
     */
    Boolean updateFirmwareVersion(String droneSn, PayloadFirmwareVersion receiver);

    /**
     * 删除基于有效载荷sn的有效载荷数据。
     * @param payloadSns
     */
    void deletePayloadsByPayloadsSn(Collection<String> payloadSns);

    /**
     * 检查设备是否具有有效负载控制功能。
     * @param deviceSn
     * @param payloadIndex
     * @return
     */
    Boolean checkAuthorityPayload(String deviceSn, String payloadIndex);

    void updatePayloadControl(DeviceDTO drone, List<DevicePayloadReceiver> payloads);
}