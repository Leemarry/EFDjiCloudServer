package com.efuav.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.efuav.manage.dao.IWorkspaceMapper;
import com.efuav.manage.model.dto.WorkspaceDTO;
import com.efuav.manage.model.entity.WorkspaceEntity;
import com.efuav.manage.service.IWorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
}
