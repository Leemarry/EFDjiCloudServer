package com.efuav.storage.controller;

import com.efuav.storage.service.IStorageService;
import com.efuav.sdk.cloudapi.storage.StsCredentialsResponse;
import com.efuav.sdk.cloudapi.storage.api.IHttpStorageService;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/29
 */
@RestController
public class StorageController implements IHttpStorageService {

    @Autowired
    private IStorageService storageService;

    /**
     * 获取用于在DJI Pilot中上传媒体和路线的临时凭据。
     *
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse<StsCredentialsResponse> getTemporaryCredential(String workspaceId, HttpServletRequest req, HttpServletResponse rsp) {
        StsCredentialsResponse stsCredentials = storageService.getSTSCredentials();
        return HttpResultResponse.success(stsCredentials);
    }
}
