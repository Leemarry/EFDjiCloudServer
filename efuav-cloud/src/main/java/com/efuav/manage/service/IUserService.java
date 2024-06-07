package com.efuav.manage.service;

import com.efuav.manage.model.dto.UserDTO;
import com.efuav.manage.model.dto.UserListDTO;
import com.efuav.manage.model.entity.UserEntity;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface IUserService {

    /**
     * 根据用户名查询用户的详细信息。
     *
     * @param username
     * @param workspaceId
     * @return
     */
    HttpResultResponse getUserByUsername(String username, String workspaceId);

    /**
     * 验证要登录的用户名和密码。
     *
     * @param username
     * @param password
     * @param flag
     * @return
     */
    HttpResultResponse userLogin(String username, String password, Integer flag);

    /**
     * 创建一个包含新令牌的用户对象。
     *
     * @param token
     * @return
     */
    Optional<UserDTO> refreshToken(String token);

    /**
     * 查询工作区中所有用户的信息。
     *
     * @param workspaceId uuid
     * @return
     */
    PaginationData<UserListDTO> getUsersByWorkspaceId(long page, long pageSize, String workspaceId);

    /**
     * 分页查询所有用户信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    PaginationData<UserListDTO> getUsers(Long page, Long pageSize);

    Boolean updateUser(String workspaceId, String userId, UserListDTO user);

    /**
     * 新增用户
     *
     * @param user 新增的用户对象
     * @return 新增是否成功
     */
    Boolean insertUser(UserEntity user);

    /**
     * 删除用户
     *
     * @param userId 用户的唯一UUID
     * @return 删除是否成功
     */
    Boolean deleteByUserId(String userId);
}
