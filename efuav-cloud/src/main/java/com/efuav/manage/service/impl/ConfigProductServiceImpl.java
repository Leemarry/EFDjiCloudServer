package com.efuav.manage.service.impl;

import com.efuav.manage.model.common.AppLicenseProperties;
import com.efuav.manage.model.common.NtpServerProperties;
import com.efuav.manage.model.dto.ProductConfigDTO;
import com.efuav.manage.service.IRequestsConfigService;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/10
 */
@Service
public class ConfigProductServiceImpl implements IRequestsConfigService {

    @Override
    public Object getConfig() {
        return new ProductConfigDTO(NtpServerProperties.host, AppLicenseProperties.id, AppLicenseProperties.key, AppLicenseProperties.license);
    }
}
