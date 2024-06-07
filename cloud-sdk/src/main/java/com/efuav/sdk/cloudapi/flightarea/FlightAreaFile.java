package com.efuav.sdk.cloudapi.flightarea;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreaFile {

    /**
     * 自定义飞行区域文件名
     */
    private String name;

    /**
     * 文件SHA256签名
     */
    private String checksum;

    public FlightAreaFile() {
    }

    @Override
    public String toString() {
        return "FlightAreaFile{" +
                "name='" + name + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public FlightAreaFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public FlightAreaFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }
}
