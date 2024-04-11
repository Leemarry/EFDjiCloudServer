package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.UserListDTO;
import com.efuav.manage.service.IUserService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;


@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/users")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 查询当前用户的信息。
     * @param request
     * @return
     */
    @GetMapping("/current")
    public HttpResultResponse getCurrentUserInfo(HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);
        return userService.getUserByUsername(customClaim.getUsername(), customClaim.getWorkspaceId());
    }

    /**
     * 分页以查询工作区中的所有用户。
     * @param page      当前页面
     * @param pageSize
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/users")
    public HttpResultResponse<PaginationData<UserListDTO>> getUsers(@RequestParam(defaultValue = "1") Long page,
                                                                    @RequestParam(value = "page_size", defaultValue = "50") Long pageSize,
                                                                    @PathVariable("workspace_id") String workspaceId) {
        PaginationData<UserListDTO> paginationData = userService.getUsersByWorkspaceId(page, pageSize, workspaceId);
        return HttpResultResponse.success(paginationData);
    }

    /**
     * 修改用户信息。只包含mqtt帐户信息，其他信息都不能修改。
     * @param user
     * @param workspaceId
     * @param userId
     * @return
     */
    @PutMapping("/{workspace_id}/users/{user_id}")
    public HttpResultResponse updateUser(@RequestBody UserListDTO user,
                                         @PathVariable("workspace_id") String workspaceId,
                                         @PathVariable("user_id") String userId) {

        userService.updateUser(workspaceId, userId, user);
        return HttpResultResponse.success();
    }
}
