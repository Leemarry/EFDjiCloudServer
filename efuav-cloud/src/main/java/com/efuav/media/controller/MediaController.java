package com.efuav.media.controller;

import com.efuav.media.service.IMediaService;
import com.efuav.sdk.cloudapi.media.*;
import com.efuav.sdk.cloudapi.media.api.IHttpMediaService;
import com.efuav.sdk.common.HttpResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
@Slf4j
@RestController
public class MediaController implements IHttpMediaService {

    @Autowired
    private IMediaService mediaService;

    /**
     * 检查文件是否已通过指纹上传。
     *
     * @param workspaceId
     * @param request
     * @return
     */
    @Override
    public HttpResultResponse mediaFastUpload(String workspaceId, @Valid MediaFastUploadRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        boolean isExist = mediaService.fastUpload(workspaceId, request.getFingerprint());

        return isExist ? HttpResultResponse.success() : HttpResultResponse.error(request.getFingerprint() + "don't exist.");
    }

    /**
     * 当通过导频将文件上载到存储服务器时，
     * 文件的基本信息是通过这个接口报告的。
     *
     * @param workspaceId
     * @param request
     * @return
     */
    @Override
    public HttpResultResponse<String> mediaUploadCallback(String workspaceId, @Valid MediaUploadCallbackRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        mediaService.saveMediaFile(workspaceId, request);
        return HttpResultResponse.success(request.getObjectKey());
    }

    /**
     * 根据工作区id和微小指纹的集合，查询此工作区中已经存在的文件。
     *
     * @param workspaceId
     * @param request     body中只有一个tiny_fingerprint参数。
     *                    但不建议使用Map来接收参数。
     * @return
     */
    @Override
    public HttpResultResponse<GetFileFingerprintResponse> getExistFileTinyFingerprint(String workspaceId, @Valid GetFileFingerprintRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        List<String> existingList = mediaService.getExistTinyFingerprints(workspaceId, request.getTinyFingerprints());
        return HttpResultResponse.success(new GetFileFingerprintResponse().setTinyFingerprints(existingList));
    }

    @Override
    public HttpResultResponse folderUploadCallback(String workspaceId, @Valid FolderUploadCallbackRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        return null;
    }
}