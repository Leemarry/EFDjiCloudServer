package com.efuav.manage.service;

import com.efuav.manage.model.dto.FirmwareModelDTO;

/**
 * @author sean
 * @version 1.3
 * @date 2022/12/21
 */
public interface IFirmwareModelService {

    /**
     * 保存固件文件和设备型号之间的关系。
     *
     * @param firmwareModel
     */
    void saveFirmwareDeviceName(FirmwareModelDTO firmwareModel);

}
