package com.efuav.component.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.efuav.component.oss.model.OssConfiguration;
import com.efuav.component.oss.service.IOssService;
import com.efuav.sdk.cloudapi.storage.CredentialsToken;
import com.efuav.sdk.cloudapi.storage.OssTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/23
 */
@Service
@Slf4j
public class AliyunOssServiceImpl implements IOssService {

    private OSS ossClient;
    
    @Override
    public OssTypeEnum getOssType() {
        return OssTypeEnum.ALIYUN;
    }

    @Override
    public CredentialsToken getCredentials() {

        try {
            DefaultProfile profile = DefaultProfile.getProfile(
                    OssConfiguration.region, OssConfiguration.accessKey, OssConfiguration.secretKey);
            IAcsClient client = new DefaultAcsClient(profile);

            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setDurationSeconds(OssConfiguration.expire);
            request.setRoleArn(OssConfiguration.roleArn);
            request.setRoleSessionName(OssConfiguration.roleSessionName);

            AssumeRoleResponse.Credentials response = client.getAcsResponse(request).getCredentials();
            return new CredentialsToken(response.getAccessKeyId(), response.getAccessKeySecret(), response.getSecurityToken(), OssConfiguration.expire);

        } catch (ClientException e) {
            log.debug("无法获取sts。");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getObjectUrl(String bucket, String objectKey) {
        // 首先检查对象是否可以提取。
        boolean isExist = ossClient.doesObjectExist(bucket, objectKey);
        if (!isExist) {
            throw new OSSException("对象不存在。");
        }

        return ossClient.generatePresignedUrl(bucket, objectKey,
                new Date(System.currentTimeMillis() + OssConfiguration.expire * 1000));
    }

    @Override
    public Boolean deleteObject(String bucket, String objectKey) {
        if (!ossClient.doesObjectExist(bucket, objectKey)) {
            return true;
        }
        ossClient.deleteObject(bucket, objectKey);
        return true;
    }

    @Override
    public InputStream getObject(String bucket, String objectKey) {
        return ossClient.getObject(bucket, objectKey).getObjectContent();
    }

    @Override
    public void putObject(String bucket, String objectKey, InputStream input) {
        if (ossClient.doesObjectExist(bucket, objectKey)) {
            throw new RuntimeException("文件名已存在。");
        }
        PutObjectResult objectResult = ossClient.putObject(new PutObjectRequest(bucket, objectKey, input, new ObjectMetadata()));
        log.info("上传FlighttaskCreateFile： {}", objectResult.getETag());
    }

    @Override
    public void createClient() {
        if (Objects.nonNull(this.ossClient)) {
            return;
        }
        this.ossClient = new OSSClientBuilder()
                .build(OssConfiguration.endpoint, OssConfiguration.accessKey, OssConfiguration.secretKey);
    }
}
