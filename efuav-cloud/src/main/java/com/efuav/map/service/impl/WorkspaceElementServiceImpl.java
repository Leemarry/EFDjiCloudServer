package com.efuav.map.service.impl;

import com.efuav.component.websocket.model.BizCodeEnum;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.map.model.dto.GroupElementDTO;
import com.efuav.map.service.IElementCoordinateService;
import com.efuav.map.service.IGroupElementService;
import com.efuav.map.service.IGroupService;
import com.efuav.map.service.IWorkspaceElementService;
import com.efuav.sdk.cloudapi.map.*;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Transactional
@Service
public class WorkspaceElementServiceImpl implements IWorkspaceElementService {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGroupElementService groupElementService;

    @Autowired
    private IElementCoordinateService elementCoordinateService;

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Override
    public List<GetMapElementsResponse> getAllGroupsByWorkspaceId(String workspaceId, String groupId, Boolean isDistributed) {
        List<GetMapElementsResponse> groupList = groupService.getAllGroupsByWorkspaceId(workspaceId, groupId, isDistributed);
        groupList.forEach(group -> group.setElements(groupElementService.getElementsByGroupId(group.getId())));
        return groupList;
    }

    @Override
    public HttpResultResponse saveElement(String workspaceId, String groupId, CreateMapElementRequest elementCreate, boolean notify) {
        boolean saveElement = groupElementService.saveElement(groupId, elementCreate);
        if (!saveElement) {
            return HttpResultResponse.error("Failed to save the element.");
        }
        if (notify) {
            // 创建元素时，通知此工作区中的所有WebSocket连接进行更新。
            getElementByElementId(elementCreate.getId())
                    .ifPresent(groupElement -> webSocketMessageService.sendBatch(
                            workspaceId, BizCodeEnum.MAP_ELEMENT_CREATE.getCode(),
                            element2CreateWsElement(groupElement)));
        }
        return HttpResultResponse.success();
    }

    @Override
    public HttpResultResponse updateElement(String workspaceId, String elementId, UpdateMapElementRequest elementUpdate, String username, boolean notify) {
        boolean updElement = groupElementService.updateElement(elementId, elementUpdate, username);
        if (!updElement) {
            return HttpResultResponse.error("Failed to update the element.");
        }

        if (notify) {
            // 当有元素更新时，通知此工作区中的所有WebSocket连接进行更新。
            getElementByElementId(elementId)
                    .ifPresent(groupElement -> webSocketMessageService.sendBatch(
                            workspaceId, BizCodeEnum.MAP_ELEMENT_UPDATE.getCode(),
                            element2UpdateWsElement(groupElement)));
        }
        return HttpResultResponse.success();
    }

    @Override
    public HttpResultResponse deleteElement(String workspaceId, String elementId, boolean notify) {
        Optional<GroupElementDTO> elementOpt = getElementByElementId(elementId);
        boolean delElement = groupElementService.deleteElement(elementId);
        if (!delElement) {
            return HttpResultResponse.error("Failed to delete the element.");
        }

        // 根据元素id删除所有坐标。
        boolean delCoordinate = elementCoordinateService.deleteCoordinateByElementId(elementId);
        if (!delCoordinate) {
            return HttpResultResponse.error("Failed to delete the coordinate.");
        }

        if (notify) {
            // 删除元素时，通知此工作区中的所有WebSocket连接进行更新。
            elementOpt.ifPresent(element ->
                    webSocketMessageService.sendBatch(workspaceId, BizCodeEnum.MAP_ELEMENT_DELETE.getCode(),
                            new MapElementDeleteWsResponse()
                                    .setGroupId(element.getGroupId())
                                    .setId(elementId)));
        }

        return HttpResultResponse.success();
    }

    @Override
    public Optional<GroupElementDTO> getElementByElementId(String elementId) {
        return groupElementService.getElementByElementId(elementId);
    }

    @Override
    public HttpResultResponse deleteAllElementByGroupId(String workspaceId, String groupId) {
        List<MapGroupElement> groupElementList = groupElementService.getElementsByGroupId(groupId);
        for (MapGroupElement groupElement : groupElementList) {
            HttpResultResponse response = this.deleteElement(workspaceId, groupElement.getId(), true);
            if (HttpResultResponse.CODE_SUCCESS != response.getCode()) {
                return response;
            }
        }

        return HttpResultResponse.success();
    }

    public MapElementCreateWsResponse element2CreateWsElement(GroupElementDTO element) {
        if (element == null) {
            return null;
        }
        return new MapElementCreateWsResponse()
                .setId(element.getElementId())
                .setGroupId(element.getGroupId())
                .setName(element.getName())
                .setResource(element.getResource())
                .setUpdateTime(element.getUpdateTime())
                .setCreateTime(element.getCreateTime());
    }

    public MapElementUpdateWsResponse element2UpdateWsElement(GroupElementDTO element) {
        if (element == null) {
            return null;
        }
        return new MapElementUpdateWsResponse()
                .setId(element.getElementId())
                .setGroupId(element.getGroupId())
                .setName(element.getName())
                .setResource(element.getResource())
                .setUpdateTime(element.getUpdateTime())
                .setCreateTime(element.getCreateTime());
    }
}
