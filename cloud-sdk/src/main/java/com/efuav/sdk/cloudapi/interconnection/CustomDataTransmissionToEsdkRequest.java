package com.efuav.sdk.cloudapi.interconnection;

import com.efuav.sdk.common.BaseModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class CustomDataTransmissionToEsdkRequest extends BaseModel {

    /**
     * 数据内容
     * 长度：小于256
     */
    @NotNull
    @Length(max = 256)
    private String value;

    public CustomDataTransmissionToEsdkRequest() {
    }

    @Override
    public String toString() {
        return "CustomDataTransmissionToEsdkRequest{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public CustomDataTransmissionToEsdkRequest setValue(String value) {
        this.value = value;
        return this;
    }
}
