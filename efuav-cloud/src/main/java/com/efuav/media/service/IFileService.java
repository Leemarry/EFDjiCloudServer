package com.efuav.media.service;

import com.efuav.media.model.MediaFileDTO;
import com.efuav.sdk.cloudapi.media.MediaUploadCallbackRequest;
import com.efuav.sdk.common.PaginationData;

import java.net.URL;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
public interface IFileService {

    /**
     * 根据工作区id和文件的指纹，查询文件是否已经存在。
     *
     * @param workspaceId
     * @param fingerprint
     * @return
     */
    Boolean checkExist(String workspaceId, String fingerprint);

    /**
     * 将文件的基本信息保存到数据库中。
     *
     * @param workspaceId
     * @param file
     * @return
     */
    Integer saveFile(String workspaceId, MediaUploadCallbackRequest file);

    /**
     * 基于工作区id查询有关此工作区中所有文件的信息。
     *
     * @param workspaceId
     * @return
     */
    List<MediaFileDTO> getAllFilesByWorkspaceId(String workspaceId);

    /**
     * 浏览此工作区中的所有媒体文件。
     *
     * @param workspaceId
     * @param page
     * @param pageSize
     * @return
     */
    PaginationData<MediaFileDTO> getMediaFilesPaginationByWorkspaceId(String workspaceId, long page, long pageSize);

    /**
     * 获取文件的下载地址。
     *
     * @param workspaceId
     * @param fileId
     * @return
     */
    URL getObjectUrl(String workspaceId, String fileId);

    /**
     * 查询作业的所有媒体文件。
     *
     * @param workspaceId
     * @param jobId
     * @return
     */
    List<MediaFileDTO> getFilesByWorkspaceAndJobId(String workspaceId, String jobId);
}
