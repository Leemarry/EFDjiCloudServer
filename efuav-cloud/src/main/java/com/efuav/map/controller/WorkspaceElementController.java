package com.efuav.map.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.map.service.IWorkspaceElementService;
import com.efuav.sdk.cloudapi.map.CreateMapElementRequest;
import com.efuav.sdk.cloudapi.map.CreateMapElementResponse;
import com.efuav.sdk.cloudapi.map.GetMapElementsResponse;
import com.efuav.sdk.cloudapi.map.UpdateMapElementRequest;
import com.efuav.sdk.cloudapi.map.api.IHttpMapService;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@RestController
public class WorkspaceElementController implements IHttpMapService {

    @Autowired
    private IWorkspaceElementService elementService;

    @Autowired
    private IWebSocketMessageService sendMessageService;

    /**
     * 根据组id删除该组中的所有元素信息。
     *
     * @param workspaceId
     * @param groupId
     * @return
     */
    @DeleteMapping("${url.map.prefix}${url.map.version}/workspaces/{workspace_id}/element-groups/{group_id}/elements")
    public HttpResultResponse deleteAllElementByGroupId(@PathVariable(name = "workspace_id") String workspaceId,
                                                        @PathVariable(name = "group_id") String groupId) {

        return elementService.deleteAllElementByGroupId(workspaceId, groupId);
    }

    /**
     * 在第一个连接中，pilot将发出这个http请求以获得组元素列表。
     * 如果pilot接收到来自WebSocket的组刷新指令，
     * 它需要相同的接口来请求组元素列表。
     *
     * @param workspaceId
     * @param groupId
     * @param isDistributed
     * @return
     */
    @Override
    public HttpResultResponse<List<GetMapElementsResponse>> getMapElements(String workspaceId, String groupId, Boolean isDistributed, HttpServletRequest req, HttpServletResponse rsp) {
        List<GetMapElementsResponse> groupsList = elementService.getAllGroupsByWorkspaceId(workspaceId, groupId, isDistributed);
        return HttpResultResponse.<List<GetMapElementsResponse>>success(groupsList);
    }

    /**
     * 当用户在PILOT/Web侧绘制点、线或多边形时。
     * 将元素信息保存到数据库中。
     *
     * @param workspaceId
     * @param groupId
     * @param elementCreate
     * @return
     */
    @Override
    public HttpResultResponse<CreateMapElementResponse> createMapElement(String workspaceId, String groupId,
                                                                         @Valid CreateMapElementRequest elementCreate, HttpServletRequest req, HttpServletResponse rsp) {
        CustomClaim claims = (CustomClaim) req.getAttribute(TOKEN_CLAIM);
        // 设置元素的创建者
        elementCreate.getResource().setUsername(claims.getUsername());

        HttpResultResponse response = elementService.saveElement(workspaceId, groupId, elementCreate, true);
        if (response.getCode() != HttpResultResponse.CODE_SUCCESS) {
            return response;
        }

        return HttpResultResponse.success(new CreateMapElementResponse().setId(elementCreate.getId()));
    }

    /**
     * 当用户在PILOT/Web侧编辑点、线或多边形时。
     * 将元素信息更新到数据库中。
     *
     * @param workspaceId
     * @param elementId
     * @param elementUpdate
     * @return
     */
    @Override
    public HttpResultResponse updateMapElement(String workspaceId, String elementId, @Valid UpdateMapElementRequest elementUpdate, HttpServletRequest req, HttpServletResponse rsp) {
        CustomClaim claims = (CustomClaim) req.getAttribute(TOKEN_CLAIM);

        HttpResultResponse response = elementService.updateElement(workspaceId, elementId, elementUpdate, claims.getUsername(), true);
        if (response.getCode() != HttpResultResponse.CODE_SUCCESS) {
            return response;
        }

        return response;
    }

    /**
     * 当用户删除PILOT/Web侧上的点、线或多边形时，
     * 删除数据库中的元素信息。
     *
     * @param workspaceId
     * @param elementId
     * @return
     */
    @Override
    public HttpResultResponse deleteMapElement(String workspaceId, String elementId, HttpServletRequest req, HttpServletResponse rsp) {

        return elementService.deleteElement(workspaceId, elementId, true);
    }
}