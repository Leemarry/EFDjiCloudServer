package com.efuav.sdk.cloudapi.map;

import com.efuav.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public class OfflineMapGetResponse extends BaseModel {

    /**
     * 此参数允许机场关闭飞机的离线地图功能。
     */
    @NotNull
    private Boolean offlineMapEnable;

    /**
     * 离线映射文件对象列表。
     */
    @NotNull
    private List<@Valid OfflineMapFile> files;

    public OfflineMapGetResponse() {
    }

    @Override
    public String toString() {
        return "OfflineMapGetResponse{" +
                "offlineMapEnable=" + offlineMapEnable +
                ", files=" + files +
                '}';
    }

    public Boolean getOfflineMapEnable() {
        return offlineMapEnable;
    }

    public OfflineMapGetResponse setOfflineMapEnable(Boolean offlineMapEnable) {
        this.offlineMapEnable = offlineMapEnable;
        return this;
    }

    public List<OfflineMapFile> getFiles() {
        return files;
    }

    public OfflineMapGetResponse setFiles(List<OfflineMapFile> files) {
        this.files = files;
        return this;
    }
}
