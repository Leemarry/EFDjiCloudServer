package com.efuav.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.efuav.manage.dao.IWorkspaceMapper;
import com.efuav.manage.model.dto.WorkspaceDTO;
import com.efuav.manage.model.entity.WorkspaceEntity;
import com.efuav.manage.service.IWorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WorkspaceServiceImpl implements IWorkspaceService {

    @Autowired
    private IWorkspaceMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<WorkspaceDTO> getWorkspaceByWorkspaceId(String workspaceId) {
        return Optional.ofNullable(entityConvertToDto(
                mapper.selectOne(
                        new LambdaQueryWrapper<WorkspaceEntity>()
                                .eq(WorkspaceEntity::getWorkspaceId, workspaceId))));
    }

    @Override
    public Optional<WorkspaceDTO> getWorkspaceNameByBindCode(String bindCode) {
        return Optional.ofNullable(entityConvertToDto(
                mapper.selectOne(new LambdaQueryWrapper<WorkspaceEntity>().eq(WorkspaceEntity::getBindCode, bindCode))));
    }

    /**
     * 将数据库实体对象转换为工作空间数据传输对象。
     *
     * @param entity
     * @return
     */
    private WorkspaceDTO entityConvertToDto(WorkspaceEntity entity) {
        if (entity == null) {
            return null;
        }
        return WorkspaceDTO.builder()
                .id(entity.getId())
                .workspaceId(entity.getWorkspaceId())
                .platformName(entity.getPlatformName())
                .workspaceDesc(entity.getWorkspaceDesc())
                .workspaceName(entity.getWorkspaceName())
                .bindCode(entity.getBindCode())
                .build();
    }

    /**
     * 新建工作空间
     *
     * @param workspaceEntity 工作空间对象
     * @return
     */
    @Override
    public boolean insertWorkSpace(WorkspaceEntity workspaceEntity) {
        if (workspaceEntity.getWorkspaceId() == null || workspaceEntity.getWorkspaceId().isEmpty()) {
            UUID uuid = UUID.randomUUID();
            workspaceEntity.setWorkspaceId(uuid.toString());
        }

        // 查询数据库中是否已存在相同 workspaceId 的记录
        WorkspaceEntity existingEntity = mapper.selectOne(new QueryWrapper<WorkspaceEntity>()
                .eq("workspace_id", workspaceEntity.getWorkspaceId()));

        // 如果不存在相同 workspaceId 的记录，则插入新记录
        if (existingEntity == null) {
            int insert = mapper.insert(workspaceEntity);
            return insert > 0;
        } else {
            // 如果存在相同 workspaceId 的记录，则返回 false 表示插入失败
            return false;
        }
    }

    /**
     * todo 删除工作空间
     *
     * @param workspaceId 工作空间id
     * @return
     */
    @Override
    public boolean deleteWorkSpace(String workspaceId) {
        // 这里可以根据workspaceId来查询数据库或其他存储，以获取更多工作空间相关的信息

        // 返回包含工作空间信息的用户身份

        return false;
    }

    /**
     * 更新工作空间信息
     * @param workspaceDTO 工作空间DTO对象
     * @return 更新是否成功的布尔值
     */
    public boolean updateWorkSpace(WorkspaceDTO workspaceDTO) {
        // 通过工作空间DTO中的工作空间ID查询对应的工作空间实体
        WorkspaceEntity workspaceEntity = mapper.selectOne(
                new LambdaQueryWrapper<WorkspaceEntity>()
                        .eq(WorkspaceEntity::getWorkspaceId, workspaceDTO.getWorkspaceId()));
        // 如果未查询到工作空间实体，则返回更新失败
        if (workspaceEntity == null) {
            return false;
        }
        // 更新工作空间实体的名称、描述、平台名称和绑定代码
        workspaceEntity.setWorkspaceName(workspaceDTO.getWorkspaceName());
        workspaceEntity.setWorkspaceDesc(workspaceDTO.getWorkspaceDesc());
        workspaceEntity.setPlatformName(workspaceDTO.getPlatformName());
        workspaceEntity.setBindCode(workspaceDTO.getBindCode());
        // 设置更新时间为当前时间戳
        workspaceEntity.setUpdateTime(System.currentTimeMillis());
        // 执行更新操作，并返回受影响的行数
        int id = mapper.update(workspaceEntity, new LambdaUpdateWrapper<WorkspaceEntity>()
                .eq(WorkspaceEntity::getWorkspaceId, workspaceDTO.getWorkspaceId()));
        // 根据受影响的行数判断更新是否成功
        return id > 0;
    }


    /**
     * 获取全部的工作空间
     *
     * @return 包含所有工作空间信息的列表
     */
    @Override
    public List<WorkspaceDTO> getAllWorkspace() {
        List<WorkspaceEntity> workspaceEntities = mapper.selectList(
                new LambdaQueryWrapper<>()
        );
        List<WorkspaceDTO> workspaceDTOs = new ArrayList<>();
        for (WorkspaceEntity entity : workspaceEntities) {
            WorkspaceDTO dto = this.entityConvertToDto(entity);
            workspaceDTOs.add(dto);
        }
        return workspaceDTOs;
    }
}
