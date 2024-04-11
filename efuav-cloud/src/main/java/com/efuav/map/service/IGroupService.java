package com.efuav.map.service;

import com.efuav.sdk.cloudapi.map.GetMapElementsResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
public interface IGroupService {

    /**
     * 根据工作区的id查询工作区中的所有组。
     * 如果组id不存在，请不要添加此筛选条件。
     *
     * @param workspaceId
     * @param groupId
     * @param isDistributed 用于定义是否需要分发组。默认值为true。
     * @return
     */
    List<GetMapElementsResponse> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed);

    /**
     * 获取此工作区下的自定义飞行区域组。
     *
     * @param workspaceId
     * @return
     */
    Optional<GetMapElementsResponse> getCustomGroupByWorkspaceId(String workspaceId);
}
