package com.efuav.storage.service.impl;

import com.efuav.component.oss.model.OssConfiguration;
import com.efuav.component.oss.service.impl.OssServiceContext;
import com.efuav.storage.service.IStorageService;
import com.efuav.sdk.cloudapi.media.StorageConfigGet;
import com.efuav.sdk.cloudapi.media.api.AbstractMediaService;
import com.efuav.sdk.cloudapi.storage.StsCredentialsResponse;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.requests.TopicRequestsRequest;
import com.efuav.sdk.mqtt.requests.TopicRequestsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/3/9
 */
@Service
public class StorageServiceImpl extends AbstractMediaService implements IStorageService {

    @Autowired
    private OssServiceContext ossService;

    @Override
    public StsCredentialsResponse getSTSCredentials() {
        return new StsCredentialsResponse()
                .setEndpoint(OssConfiguration.endpoint)
                .setBucket(OssConfiguration.bucket)
                .setCredentials(ossService.getCredentials())
                .setProvider(OssConfiguration.provider)
                .setObjectKeyPrefix(OssConfiguration.objectDirPrefix)
                .setRegion(OssConfiguration.region);
    }

    @Override
    public TopicRequestsResponse<MqttReply<StsCredentialsResponse>> storageConfigGet(TopicRequestsRequest<StorageConfigGet> response, MessageHeaders headers) {
        return new TopicRequestsResponse<MqttReply<StsCredentialsResponse>>().setData(MqttReply.success(getSTSCredentials()));
    }
}
