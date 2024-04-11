package com.efuav.manage.service;

import com.efuav.manage.model.dto.DeviceLogsDTO;
import com.efuav.manage.model.param.DeviceLogsCreateParam;
import com.efuav.manage.model.param.DeviceLogsQueryParam;
import com.efuav.sdk.cloudapi.log.FileUploadUpdateRequest;
import com.efuav.sdk.cloudapi.log.LogModuleEnum;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public interface IDeviceLogsService {

    /**
     * 根据查询参数进行分页，得到设备上传日志列表。
     * @param deviceSn
     * @param param
     * @return
     */
    PaginationData<DeviceLogsDTO> getUploadedLogs(String deviceSn, DeviceLogsQueryParam param);

    /**
     * 获取可以实时上传的日志文件列表。
     * @param deviceSn
     * @param domainList
     * @return
     */
    HttpResultResponse getRealTimeLogs(String deviceSn, List<LogModuleEnum> domainList);

    /**
     * 添加设备日志。
     *
     * @param bid
     * @param username
     * @param deviceSn
     * @param param
     * @return logs id
     */
    String insertDeviceLogs(String bid, String username, String deviceSn, DeviceLogsCreateParam param);

    /**
     * 向网关发起日志上传请求。
     * @param username
     * @param deviceSn
     * @param param
     * @return
     */
    HttpResultResponse pushFileUpload(String username, String deviceSn, DeviceLogsCreateParam param);

    /**
     * 推送请求以修改日志文件的状态。
     * @param deviceSn
     * @param param
     * @return
     */
    HttpResultResponse pushUpdateFile(String deviceSn, FileUploadUpdateRequest param);

    /**
     * 删除日志记录。
     * @param deviceSn
     * @param logsId
     */
    void deleteLogs(String deviceSn, String logsId);

    /**
     * 更新状态，当日志上传成功或失败时更新。
     * @param logsId
     * @param value
     */
    void updateLogsStatus(String logsId, Integer value);

    /**
     * 获取文件地址。
     * @param logsId
     * @param fileId
     * @return
     */
    URL getLogsFileUrl(String logsId, String fileId);
}
