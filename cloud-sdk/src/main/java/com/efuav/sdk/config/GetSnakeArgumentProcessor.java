package com.efuav.sdk.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/16
 */
public class GetSnakeArgumentProcessor extends ServletModelAttributeMethodProcessor {

    /**
     * 类构造函数。
     *
     * @param annotationNotRequired 如果为“true”，则非简单方法参数和
     *                              返回值被视为具有或不具有的模型属性
     *                              ｛@code@ModelAttribute｝注释
     */
    public GetSnakeArgumentProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        super.bindRequestParameters(new GetSnakeDataBinder(binder.getTarget(), binder.getObjectName()), request);
    }
}
