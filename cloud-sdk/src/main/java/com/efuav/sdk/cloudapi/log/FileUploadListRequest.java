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
public class FileUploadListRequest extends BaseModel {

    /**
     *文件的筛选器列表
     **/
    @NotNull
    @Size(min = 1, max = 2)
    private List<LogModuleEnum> moduleList;

    public FileUploadListRequest() {
    }

    @Override
    public String toString() {
        return "FileUploadListRequest{" +
                "moduleList=" + moduleList +
                '}';
    }

    public List<LogModuleEnum> getModuleList() {
        return moduleList;
    }

    public FileUploadListRequest setModuleList(List<LogModuleEnum> moduleList) {
        this.moduleList = moduleList;
        return this;
    }
}
