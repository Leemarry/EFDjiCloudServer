package com.efuav.manage.service;


import com.efuav.manage.model.dto.WorkspaceDTO;
import com.efuav.manage.model.entity.WorkspaceEntity;

import java.util.List;
import java.util.Optional;

public interface IWorkspaceService {

    /**
     * 根据工作区id查询工作区的信息。
     *
     * @param workspaceId
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId);

    /**
     * 基于绑定代码查询工作区的工作区。
     *
     * @param bindCode
     * @return
     */
    Optional<WorkspaceDTO> getWorkspaceNameByBindCode(String bindCode);

    /**
     * 新建工作空间
     *
     * @param workspaceEntity 工作空间对象
     * @return
     */
    boolean insertWorkSpace(WorkspaceEntity workspaceEntity);

    /**
     * 删除工作空间
     *
     * @param workspaceId 工作空间id
     * @return
     */
    boolean deleteWorkSpace(String workspaceId);

    /**
     * 修改工作空间信息
     *
     * @param workspaceDTO
     * @return
     */
    boolean updateWorkSpace(WorkspaceDTO workspaceDTO);

    /**
     * 查询全部工作空间信息
     *
     * @return
     */
    List<WorkspaceDTO> getAllWorkspace();
}
