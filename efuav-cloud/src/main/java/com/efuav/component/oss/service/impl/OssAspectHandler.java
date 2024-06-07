package com.efuav.component.oss.service.impl;

import com.efuav.component.oss.model.OssConfiguration;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/20
 */
@Component
@Aspect
public class OssAspectHandler {

    @Autowired
    private OssServiceContext ossServiceContext;

    @Before("execution(public * com.efuav.component.oss.service.impl.OssServiceContext.*(..))")
    public void before() {
        if (!OssConfiguration.enable) {
            throw new IllegalArgumentException("请启用OssConfiguration。");
        }
        if (this.ossServiceContext.getOssService() == null) {
            throw new IllegalArgumentException("请检查OssConfiguration配置。");
        }
        this.ossServiceContext.createClient();
    }
}
