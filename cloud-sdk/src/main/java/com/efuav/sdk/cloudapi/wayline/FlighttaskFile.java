package com.efuav.sdk.cloudapi.wayline;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class FlighttaskFile {

    /**
     * 文件URL
     */
    @NotNull
    private String url;

    /**
     * 文件签名
     */
    @NotNull
    private String fingerprint;

    public FlighttaskFile() {
    }

    @Override
    public String toString() {
        return "FlighttaskFile{" +
                "url='" + url + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public FlighttaskFile setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public FlighttaskFile setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }
}
