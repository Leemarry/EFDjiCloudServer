package com.efuav.common.error;

import com.efuav.sdk.common.IErrorInfo;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum CommonErrorEnum implements IErrorInfo {

    ILLEGAL_ARGUMENT(200001, "非法参数"),

    REDIS_DATA_NOT_FOUND(201404, "Redis数据不存在"),

    DEVICE_OFFLINE(212015, "设备处于离线状态"),

    GET_ORGANIZATION_FAILED(210230, "无法获取组织."),

    DEVICE_BINDING_FAILED(210231, "绑定设备失败."),

    NON_REPEATABLE_BINDING(210232, "设备已绑定到另一个组织，无法重复绑定"),

    GET_DEVICE_BINDING_STATUS_FAILED(210233, "无法获取设备绑定状态"),

    SYSTEM_ERROR(600500, "系统错误"),

    SECRET_INVALID(600100, "机密无效"),

    NO_TOKEN(600101, "令牌为null"),

    TOKEN_EXPIRED(600102, "令牌已过期"),

    TOKEN_INVALID(600103, "令牌无效"),

    SIGN_INVALID(600104, "符号无效");

    private String msg;

    private int code;

    CommonErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

}
