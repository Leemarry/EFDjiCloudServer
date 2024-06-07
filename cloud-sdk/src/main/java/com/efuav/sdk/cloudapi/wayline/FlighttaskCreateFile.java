package com.efuav.sdk.cloudapi.wayline;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskCreateFile {

    /**
     * 文件URL
     */
    @NotNull
    private String url;

    /**
     * MD5签名
     */
    @NotNull
    private String sign;

    public FlighttaskCreateFile() {
    }

    @Override
    public String toString() {
        return "FlighttaskCreateFile{" +
                "url='" + url + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public FlighttaskCreateFile setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public FlighttaskCreateFile setSign(String sign) {
        this.sign = sign;
        return this;
    }
}