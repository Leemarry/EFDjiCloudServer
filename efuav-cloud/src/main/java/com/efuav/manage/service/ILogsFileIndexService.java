package com.efuav.manage.service;

import com.efuav.manage.model.dto.LogsFileDTO;
import com.efuav.manage.model.dto.LogsFileUploadDTO;
import com.efuav.sdk.cloudapi.log.LogFileIndex;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
public interface ILogsFileIndexService {

    /**
     * 插入设备日志的索引。
     *
     * @param file
     * @param deviceSn
     * @param domain
     * @param fileId
     * @return
     */
    Boolean insertFileIndex(LogFileIndex file, String deviceSn, Integer domain, String fileId);

    /**
     * 查询基于文件id记录文件上传信息。
     *
     * @param fileId
     * @return
     */
    Optional<LogsFileUploadDTO> getFileIndexByFileId(String fileId);

    /**
     * 批量查询日志文件上传信息。
     *
     * @param fileIds
     * @return
     */
    List<LogsFileUploadDTO> getFileIndexByFileIds(List<LogsFileDTO> fileIds);

    /**
     * 根据文件id删除日志索引数据。
     *
     * @param fileIds
     */
    void deleteFileIndexByFileIds(List<String> fileIds);
}
