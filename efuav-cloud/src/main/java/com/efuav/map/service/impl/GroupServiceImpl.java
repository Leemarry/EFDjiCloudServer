package com.efuav.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.efuav.map.dao.IGroupMapper;
import com.efuav.map.model.entity.GroupEntity;
import com.efuav.map.service.IGroupElementService;
import com.efuav.map.service.IGroupService;
import com.efuav.sdk.cloudapi.map.GetMapElementsResponse;
import com.efuav.sdk.cloudapi.map.GroupTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Service
@Transactional
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private IGroupMapper mapper;

    @Autowired
    private IGroupElementService groupElementService;

    @Override
    public List<GetMapElementsResponse> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed) {

        return mapper.selectList(
                new LambdaQueryWrapper<GroupEntity>()
                        .eq(GroupEntity::getWorkspaceId, workspaceId)
                        .eq(StringUtils.hasText(groupId), GroupEntity::getGroupId, groupId)
                        .eq(isDistributed != null, GroupEntity::getIsDistributed, isDistributed))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GetMapElementsResponse> getCustomGroupByWorkspaceId(String workspaceId) {
        return Optional.ofNullable(mapper.selectOne(
                Wrappers.lambdaQuery(GroupEntity.class)
                        .eq(GroupEntity::getWorkspaceId, workspaceId)
                        .eq(GroupEntity::getGroupType, GroupTypeEnum.CUSTOM.getType())
                        .last(" limit 1")))
                .map(this::entityConvertToDto);
    }

    /**
     * 将数据库实体对象转换为组数据传输对象。
     * @param entity
     * @return
     */
    private GetMapElementsResponse entityConvertToDto(GroupEntity entity) {
        if (entity == null) {
            return null;
        }

        return new GetMapElementsResponse()
                .setId(entity.getGroupId())
                .setName(entity.getGroupName())
                .setType(GroupTypeEnum.find(entity.getGroupType()))
                .setLock(entity.getIsLock());
    }
}
