package com.efuav.manage.service;

import com.efuav.manage.model.dto.DeviceHmsDTO;
import com.efuav.manage.model.param.DeviceHmsQueryParam;
import com.efuav.sdk.common.PaginationData;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/6
 */
public interface IDeviceHmsService {

    /**
     * 根据查询参数进行分页查询hms数据。
     * @param param
     * @return
     */
    PaginationData<DeviceHmsDTO> getDeviceHmsByParam(DeviceHmsQueryParam param);

    /**
     * 读取消息处理。
     * @param deviceSn
     */
    void updateUnreadHms(String deviceSn);
}
