package com.efuav.sdk.cloudapi.interconnection;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public class CustomDataTransmissionFromPsdk {

    /**
     * 数据内容
     * 长度：小于256
     */
    private String value;

    public CustomDataTransmissionFromPsdk() {
    }

    @Override
    public String toString() {
        return "CustomDataTransmissionFromPsdk{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public CustomDataTransmissionFromPsdk setValue(String value) {
        this.value = value;
        return this;
    }
}
