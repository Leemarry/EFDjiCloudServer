package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.UserListDTO;
import com.efuav.manage.model.entity.UserEntity;
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
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public HttpResultResponse getCurrentUserInfo(HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        return userService.getUserByUsername(customClaim.getUsername(), customClaim.getWorkspaceId());
    }

    /**
     * 分页以查询工作区中的所有用户。
     *
     * @param page        当前页面
     * @param pageSize
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/users")
    public HttpResultResponse<PaginationData<UserListDTO>> getUsers(@RequestParam(defaultValue = "1") Long page,
                                                                    @RequestParam(value = "page_size", defaultValue = "50") Long pageSize,
                                                                    @PathVariable("workspace_id") String workspaceId, HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        if ("d14a3689-98e8-4c9f-b839-962056555149".equals(customClaim.getId())) {
            PaginationData<UserListDTO> paginationData = userService.getUsers(page, pageSize);
            return HttpResultResponse.success(paginationData);
        }
        PaginationData<UserListDTO> paginationData = userService.getUsersByWorkspaceId(page, pageSize, workspaceId);
        return HttpResultResponse.success(paginationData);
    }

    /**
     * 修改用户信息。只包含mqtt帐户信息，其他信息都不能修改。
     *
     * @param user
     * @param workspaceId
     * @param userId
     * @return
     */
    @PutMapping("/{workspace_id}/users/{user_id}/update")
    public HttpResultResponse updateUser(@RequestBody UserListDTO user,
                                         @PathVariable("workspace_id") String workspaceId,
                                         @PathVariable("user_id") String userId) {
        userService.updateUser(workspaceId, userId, user);
        return HttpResultResponse.success();
    }

    /**
     * 新增用户
     *
     * @param userEntity 输入的用户信息
     * @return
     */
    @PostMapping("/{workspace_id}/users/{user_id}/insert")
    public HttpResultResponse insertUser(@RequestBody UserEntity userEntity) {
        Boolean aBoolean = userService.insertUser(userEntity);
        if (aBoolean) {
            return HttpResultResponse.success("新增用户成功！");
        } else {
            return HttpResultResponse.error("新增用户失败，请稍后重试！");
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户唯一UUID
     * @return
     */
    @DeleteMapping("/{workspace_id}/users/{user_id}/delete")
    public HttpResultResponse deleteUser(@RequestParam String userId) {
        Boolean aBoolean = userService.deleteByUserId(userId);
        if (aBoolean) {
            return HttpResultResponse.success("删除用户成功！");
        } else {
            return HttpResultResponse.error("删除用户失败，请稍后重试！");
        }
    }
}
