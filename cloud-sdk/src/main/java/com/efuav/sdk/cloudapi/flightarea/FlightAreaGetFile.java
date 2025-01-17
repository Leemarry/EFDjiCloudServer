package com.efuav.sdk.cloudapi.flightarea;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreaGetFile {

    /**
     * 文件名
     */
    @NotNull
    @Pattern(regexp = "^geofence_[A-Za-z0-9]{32}.json$")
    private String name;

    /**
     * 文件URL
     */
    @NotNull
    private String url;

    /**
     * 文件SHA256签名
     */
    @NotNull
    private String checksum;

    /**
     * 文件大小
     */
    @NotNull
    private Integer size;

    public FlightAreaGetFile() {
    }

    @Override
    public String toString() {
        return "FlightAreaGetFile{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", checksum='" + checksum + '\'' +
                ", size=" + size +
                '}';
    }

    public String getName() {
        return name;
    }

    public FlightAreaGetFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FlightAreaGetFile setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public FlightAreaGetFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public FlightAreaGetFile setSize(Integer size) {
        this.size = size;
        return this;
    }
}
