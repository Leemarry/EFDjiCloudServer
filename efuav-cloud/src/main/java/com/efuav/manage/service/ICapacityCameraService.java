package com.efuav.manage.service;

import com.efuav.manage.model.dto.CapacityCameraDTO;
import com.efuav.manage.model.receiver.CapacityCameraReceiver;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface ICapacityCameraService {

    /**
     * 基于设备sn查询可以从该设备直播的所有相机数据。
     * @param deviceSn
     * @return
     */
    List<CapacityCameraDTO> getCapacityCameraByDeviceSn(String deviceSn);

    /**
     * 基于设备sn删除此设备的所有实时功能数据。
     * @param deviceSn
     * @return
     */
    Boolean deleteCapacityCameraByDeviceSn(String deviceSn);

    /**
     * 保存设备的实时功能数据。
     * @param capacityCameraReceivers
     * @param deviceSn
     */
    void saveCapacityCameraReceiverList(List<CapacityCameraReceiver> capacityCameraReceivers, String deviceSn);

    /**
     * 将接收到的摄像机能力对象转换为摄像机数据传输对象。
     * @param receiver
     * @return
     */
    CapacityCameraDTO receiver2Dto(CapacityCameraReceiver receiver);
}
