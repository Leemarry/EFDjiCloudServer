package com.efuav.sdk.cloudapi.log;

import com.efuav.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public class FileUploadUpdateRequest extends BaseModel {

    /**
     * 文件的筛选器列表
     **/
    @NotNull
    @Size(min = 1, max = 2)
    private List<LogModuleEnum> moduleList;

    @NotNull
    private FileUploadUpdateStatusEnum status;

    public FileUploadUpdateRequest() {
    }

    @Override
    public String toString() {
        return "FileUploadUpdateRequest{" +
                "moduleList=" + moduleList +
                ", status=" + status +
                '}';
    }

    public List<LogModuleEnum> getModuleList() {
        return moduleList;
    }

    public FileUploadUpdateRequest setModuleList(List<LogModuleEnum> moduleList) {
        this.moduleList = moduleList;
        return this;
    }

    public FileUploadUpdateStatusEnum getStatus() {
        return status;
    }

    public FileUploadUpdateRequest setStatus(FileUploadUpdateStatusEnum status) {
        this.status = status;
        return this;
    }
}
