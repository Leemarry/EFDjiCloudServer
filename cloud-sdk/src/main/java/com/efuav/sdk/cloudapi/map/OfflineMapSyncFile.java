package com.efuav.sdk.cloudapi.map;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapSyncFile {

    /**
     * 离线映射文件名将用作确定版本的一种方式，格式为：offline_map_{sync_method}_版本
     * offline_map:是固定前缀，sync_method:数据同步方法-完整（full），版本：版本号
     */
    private String name;

    /**
     * 使用SHA256计算，此值可用于确认文件是否完整。
     */
    private String checksum;

    public OfflineMapSyncFile() {
    }

    @Override
    public String toString() {
        return "OfflineMapSyncFile{" +
                "name='" + name + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public OfflineMapSyncFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public OfflineMapSyncFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }
}
