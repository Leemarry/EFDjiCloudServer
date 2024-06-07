package com.efuav.manage.service;

import com.efuav.manage.model.dto.LogsFileDTO;
import com.efuav.manage.model.dto.LogsFileUploadDTO;
import com.efuav.sdk.cloudapi.log.FileUploadProgressFile;
import com.efuav.sdk.cloudapi.log.FileUploadStartFile;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
public interface ILogsFileService {

    /**
     * 根据日志id查询上传的日志文件信息。
     *
     * @param logsId
     * @return
     */
    List<LogsFileDTO> getLogsFileInfoByLogsId(String logsId);

    /**
     * 根据日志id查询上传的日志文件结构信息。
     *
     * @param logsId
     * @return
     */
    List<LogsFileUploadDTO> getLogsFileByLogsId(String logsId);

    /**
     * 添加日志文件。
     *
     * @param file
     * @param logsId
     * @return
     */
    Boolean insertFile(FileUploadStartFile file, String logsId);

    /**
     * 删除日志文件。
     *
     * @param logsId
     */
    void deleteFileByLogsId(String logsId);

    /**
     * 更新文件信息。
     *
     * @param logsId
     * @param fileReceiver
     */
    void updateFile(String logsId, FileUploadProgressFile fileReceiver);

    /**
     * 更新文件上传状态。
     *
     * @param logsId
     * @param isUploaded
     */
    void updateFileUploadStatus(String logsId, Boolean isUploaded);

    /**
     * 获取文件地址。
     *
     * @param logsId
     * @param fileId
     * @return
     */
    URL getLogsFileUrl(String logsId, String fileId);
}
