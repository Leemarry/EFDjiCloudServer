package com.efuav.component.oss.service;

import com.efuav.sdk.cloudapi.storage.CredentialsToken;
import com.efuav.sdk.cloudapi.storage.OssTypeEnum;

import java.io.InputStream;
import java.net.URL;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
public interface IOssService {

    OssTypeEnum getOssType();

    /**
     * 获取临时凭据。
     * @return
     */
    CredentialsToken getCredentials();

    /**
     * 根据bucket名称和对象名称获取对象的地址。
     * @param bucket    bucket name
     * @param objectKey object name
     * @return download link
     */
    URL getObjectUrl(String bucket, String objectKey);

    /**
     * 删除存储桶中的对象。
     * @param bucket
     * @param objectKey
     * @return
     */
    Boolean deleteObject(String bucket, String objectKey);

    /**
     * 获取对象的内容。
     * @param bucket
     * @param objectKey
     * @return
     */
    InputStream getObject(String bucket, String objectKey);

    void putObject(String bucket, String objectKey, InputStream input);

    void createClient();
}
