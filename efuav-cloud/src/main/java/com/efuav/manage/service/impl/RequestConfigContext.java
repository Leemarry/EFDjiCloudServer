package com.efuav.manage.service.impl;

import com.efuav.common.error.CommonErrorEnum;
import com.efuav.common.util.SpringBeanUtilsTest;
import com.efuav.manage.model.dto.ProductConfigDTO;
import com.efuav.manage.model.enums.CustomizeConfigScopeEnum;
import com.efuav.sdk.cloudapi.config.ProductConfigResponse;
import com.efuav.sdk.cloudapi.config.RequestsConfigRequest;
import com.efuav.sdk.cloudapi.config.api.AbstractConfigService;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.requests.TopicRequestsRequest;
import com.efuav.sdk.mqtt.requests.TopicRequestsResponse;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Service
public class RequestConfigContext extends AbstractConfigService {

    @Override
    public TopicRequestsResponse<ProductConfigResponse> requestsConfig(TopicRequestsRequest<RequestsConfigRequest> request, MessageHeaders headers) {
        RequestsConfigRequest configReceiver = request.getData();
        Optional<CustomizeConfigScopeEnum> scopeEnumOpt = CustomizeConfigScopeEnum.find(configReceiver.getConfigScope().getScope());
        if (scopeEnumOpt.isEmpty()) {
            return new TopicRequestsResponse().setData(MqttReply.error(CommonErrorEnum.ILLEGAL_ARGUMENT));
        }

        ProductConfigDTO config = (ProductConfigDTO) SpringBeanUtilsTest.getBean(scopeEnumOpt.get().getClazz()).getConfig();
        return new TopicRequestsResponse<ProductConfigResponse>().setData(
                new ProductConfigResponse()
                        .setNtpServerHost(config.getNtpServerHost())
                        .setAppId(config.getAppId())
                        .setAppKey(config.getAppKey())
                        .setAppLicense(config.getAppLicense()));
    }
}
