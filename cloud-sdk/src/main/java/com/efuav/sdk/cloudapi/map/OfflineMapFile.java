package com.efuav.sdk.cloudapi.map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapFile {

    /**
     * 离线映射文件名将用作确定版本的一种方式，格式为：offline_map_{sync_method}_版本
     * offline_map:是固定前缀，sync_method:数据同步方法-完整（full），版本：版本号
     */
    @NotNull
    @Pattern(regexp = "^offline_map_full_\\w+\\.rocksdb\\.zip$")
    private String name;

    @NotNull
    private String url;

    /**
     * 使用SHA256计算，此值可用于确认文件是否完整。
     */
    @NotNull
    private String checksum;

    /**
     * 此文件的大小（以字节为单位）。
     */
    @NotNull
    private Long size;

    public OfflineMapFile() {
    }

    @Override
    public String toString() {
        return "OfflineMapFile{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", checksum='" + checksum + '\'' +
                ", size=" + size +
                '}';
    }

    public String getName() {
        return name;
    }

    public OfflineMapFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public OfflineMapFile setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public OfflineMapFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public OfflineMapFile setSize(Long size) {
        this.size = size;
        return this;
    }
}
