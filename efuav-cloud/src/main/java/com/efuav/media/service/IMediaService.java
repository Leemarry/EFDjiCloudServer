package com.efuav.media.service;

import com.efuav.sdk.cloudapi.media.MediaUploadCallbackRequest;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
public interface IMediaService {

    /**
     * 检查文件是否已通过指纹上传。
     *
     * @param workspaceId
     * @param fingerprint
     * @return
     */
    Boolean fastUpload(String workspaceId, String fingerprint);

    /**
     * 将文件的基本信息保存到数据库中。
     *
     * @param workspaceId
     * @param file
     * @return
     */
    Integer saveMediaFile(String workspaceId, MediaUploadCallbackRequest file);

    /**
     * 根据工作区id查询此工作区中所有文件的微小指纹。
     *
     * @param workspaceId
     * @return
     */
    List<String> getAllTinyFingerprintsByWorkspaceId(String workspaceId);

    /**
     * 根据传入的微小指纹数据查询其中已经存在的指纹。
     *
     * @param workspaceId
     * @param tinyFingerprints
     * @return
     */
    List<String> getExistTinyFingerprints(String workspaceId, List<String> tinyFingerprints);

}
