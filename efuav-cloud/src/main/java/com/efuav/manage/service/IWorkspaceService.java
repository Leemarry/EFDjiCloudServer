package com.efuav.manage.service;


import com.efuav.manage.model.dto.WorkspaceDTO;

import java.util.Optional;

public interface IWorkspaceService {

    /**
     * 根据工作区id查询工作区的信息。
     * @param workspaceId
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId);

    /**
     * 基于绑定代码查询工作区的工作区。
     * @param bindCode
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceNameByBindCode(String bindCode);

}
