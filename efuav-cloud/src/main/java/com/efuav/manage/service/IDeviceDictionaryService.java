package com.efuav.manage.service;

import com.efuav.manage.model.dto.DeviceDictionaryDTO;

import java.util.Optional;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
public interface IDeviceDictionaryService {

    /**
     * 根据域、设备类型和子类型查询设备的类型数据。
     *
     * @param domain
     * @param deviceType
     * @param subType
     * @return
     */
    Optional<DeviceDictionaryDTO> getOneDictionaryInfoByTypeSubType(Integer domain, Integer deviceType, Integer subType);

}
