package com.efuav.sdk.common;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public interface IErrorInfo {

    /**
     * 获取错误消息。
     *
     * @return error message
     */
    String getMessage();

    /**
     * 获取错误代码。
     *
     * @return error code
     */
    Integer getCode();

}
