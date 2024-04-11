package com.efuav.map.service;

import com.efuav.map.model.dto.GroupElementDTO;
import com.efuav.sdk.cloudapi.map.*;
import com.efuav.sdk.common.HttpResultResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
public interface IWorkspaceElementService {

    /**
     * 根据工作区的id查询工作区中的所有组，
     * 包括组中元素的信息和元素中的坐标信息。
     *
     * @param workspaceId
     * @param groupId
     * @param isDistributed
     * @return
     */
    List<GetMapElementsResponse> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed);

    /**
     * 保存所有元素，包括组中元素的信息，
     * 以及元素中的坐标信息。
     *
     * @param workspaceId
     * @param groupId
     * @param elementCreate
     * @param notify
     * @return
     */
    HttpResultResponse saveElement(String workspaceId, String groupId, CreateMapElementRequest elementCreate, boolean notify);

    /**
     * 基于元素id更新元素信息，
     * 包括组中元素的信息和元素中的坐标信息。
     *
     * @param workspaceId
     * @param elementId
     * @param elementUpdate
     * @param username
     * @param notify
     * @return
     */
    HttpResultResponse updateElement(String workspaceId, String elementId, UpdateMapElementRequest elementUpdate, String username, boolean notify);

    /**
     * 删除基于元素id的元素信息，
     * 包括组中元素的信息和元素中的坐标信息。
     *
     * @param workspaceId
     * @param elementId
     * @param notify
     * @return
     */
    HttpResultResponse deleteElement(String workspaceId, String elementId, boolean notify);

    /**
     * 根据元素id查询元素，
     * 包括组中元素的信息和元素中的坐标信息。
     *
     * @param elementId
     * @return
     */
    Optional<GroupElementDTO> getElementByElementId(String elementId);

    /**
     * 删除基于组id的所有元素信息，
     * 包括组中元素的信息和元素中的坐标信息。
     *
     * @param workspaceId
     * @param groupId
     * @return
     */
    HttpResultResponse deleteAllElementByGroupId(String workspaceId, String groupId);

    MapElementCreateWsResponse element2CreateWsElement(GroupElementDTO element);

    MapElementUpdateWsResponse element2UpdateWsElement(GroupElementDTO element);
}