package com.efuav.storage.service;

import com.efuav.sdk.cloudapi.storage.StsCredentialsResponse;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/29
 */
public interface IStorageService {

    /**
     * 获取用于上传媒体和路线的自定义临时凭据对象。
     *
     * @return 临时凭据对象
     */
    StsCredentialsResponse getSTSCredentials();

}
