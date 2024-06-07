package com.efuav.manage.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efuav.common.model.CustomClaim;
import com.efuav.common.util.JwtUtil;
import com.efuav.component.mqtt.config.MqttPropertyConfiguration;
import com.efuav.manage.dao.IUserMapper;
import com.efuav.manage.model.dto.UserDTO;
import com.efuav.manage.model.dto.UserListDTO;
import com.efuav.manage.model.dto.WorkspaceDTO;
import com.efuav.manage.model.entity.UserEntity;
import com.efuav.manage.model.enums.UserTypeEnum;
import com.efuav.manage.service.IUserService;
import com.efuav.manage.service.IWorkspaceService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.Pagination;
import com.efuav.sdk.common.PaginationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper mapper;

    @Autowired
    private MqttPropertyConfiguration mqttPropertyConfiguration;

    @Autowired
    private IWorkspaceService workspaceService;

    @Override
    public HttpResultResponse getUserByUsername(String username, String workspaceId) {

        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("无效的用户名。");
        }

        UserDTO user = this.entityConvertToDTO(userEntity);
        user.setWorkspaceId(workspaceId);

        return HttpResultResponse.success(user);
    }

    @Override
    public HttpResultResponse userLogin(String username, String password, Integer flag) {
        // 检查登录用户
        UserEntity userEntity = this.getUserByUsername(username);
        if (userEntity == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("无效的用户名");
        }
        if (flag.intValue() != userEntity.getUserType().intValue()) {
            return HttpResultResponse.error("帐户类型不匹配。");
        }
        if (!password.equals(userEntity.getPassword())) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("无效的登录密码。");
        }

        Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(userEntity.getWorkspaceId());
        if (workspaceOpt.isEmpty()) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("无效的工作空间ID");
        }

        CustomClaim customClaim = new CustomClaim(userEntity.getUserId(),
                userEntity.getUsername(), userEntity.getUserType(),
                workspaceOpt.get().getWorkspaceId());

        // 创建令牌
        String token = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO userDTO = entityConvertToDTO(userEntity);
        userDTO.setMqttAddr(MqttPropertyConfiguration.getBasicMqttAddress());
        userDTO.setAccessToken(token);
        userDTO.setWorkspaceId(workspaceOpt.get().getWorkspaceId());
        return HttpResultResponse.success(userDTO);
    }

    @Override
    public Optional<UserDTO> refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Optional.empty();
        }
        CustomClaim customClaim;
        try {
            DecodedJWT jwt = JwtUtil.verifyToken(token);
            customClaim = new CustomClaim(jwt.getClaims());
        } catch (TokenExpiredException e) {
            customClaim = new CustomClaim(JWT.decode(token).getClaims());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        String refreshToken = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO user = entityConvertToDTO(this.getUserByUsername(customClaim.getUsername()));
        if (Objects.isNull(user)) {
            return Optional.empty();
        }
        user.setWorkspaceId(customClaim.getWorkspaceId());
        user.setAccessToken(refreshToken);
        return Optional.of(user);
    }

    @Override
    public PaginationData<UserListDTO> getUsersByWorkspaceId(long page, long pageSize, String workspaceId) {
        Page<UserEntity> userEntityPage = mapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getWorkspaceId, workspaceId));

        List<UserListDTO> usersList = userEntityPage.getRecords()
                .stream()
                .map(this::entity2UserListDTO)
                .collect(Collectors.toList());
        return new PaginationData<>(usersList, new Pagination(userEntityPage.getCurrent(), userEntityPage.getSize(), userEntityPage.getTotal()));
    }

    /**
     * 分页查询所有用户信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PaginationData<UserListDTO> getUsers(Long page, Long pageSize) {
        Page<UserEntity> userEntityPage = mapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<UserEntity>());
        List<UserListDTO> usersList = userEntityPage.getRecords()
                .stream()
                .map(this::entity2UserListDTO)
                .collect(Collectors.toList());
        return new PaginationData<>(usersList, new Pagination(userEntityPage.getCurrent(), userEntityPage.getSize(), userEntityPage.getTotal()));
    }

    @Override
    public Boolean updateUser(String workspaceId, String userId, UserListDTO user) {
        UserEntity userEntity = mapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUserId, userId)
                        .eq(UserEntity::getWorkspaceId, workspaceId));
        if (userEntity == null) {
            return false;
        }
        userEntity.setMqttUsername(user.getMqttUsername());
        userEntity.setMqttPassword(user.getMqttPassword());
        userEntity.setUpdateTime(System.currentTimeMillis());
        int id = mapper.update(userEntity, new LambdaUpdateWrapper<UserEntity>()
                .eq(UserEntity::getUserId, userId)
                .eq(UserEntity::getWorkspaceId, workspaceId));

        return id > 0;
    }

    //region 用户数据表操作

    /**
     * 按用户名查询用户。
     *
     * @param username
     * @return
     */
    private UserEntity getUserByUsername(String username) {
        return mapper.selectOne(new QueryWrapper<UserEntity>()
                .eq("username", username));
    }

    /**
     * 按UserId查询用户。
     *
     * @param userId
     * @return
     */
    private UserEntity getUserByUserId(String userId) {
        return mapper.selectOne(new QueryWrapper<UserEntity>()
                .eq("user_id", userId));
    }

    /**
     * 按用户名和工作空间ID查询用户。
     *
     * @param username    用户名
     * @param workspaceId 工作空间ID
     * @return 匹配的用户实体，如果不存在则返回null
     */
    private UserEntity getUserByUsernameAndWorkSpaceId(String username, String workspaceId) {
        return mapper.selectOne(new QueryWrapper<UserEntity>()
                // 修改为按用户名查询
                .eq("username", username)
                // 添加按工作空间ID查询条件
                .eq("workspace_id", workspaceId));
    }


    /**
     * 新增用户
     *
     * @param user 新增的用户对象
     * @return 新增是否成功
     */
    @Override
    public Boolean insertUser(UserEntity user) {
        if (user.getUsername().isEmpty()) {
            log.info("新增用户：无效的数据，请核对后再尝试！");
            return false;
        }
        if (user.getWorkspaceId().isEmpty()) {
            //没有工作空间需要新增
            UUID uuid = UUID.randomUUID();
            user.setWorkspaceId(uuid.toString());
        }
        UserEntity userEntity = this.getUserByUsernameAndWorkSpaceId(user.getUsername(), user.getWorkspaceId());
        if (userEntity != null) {
            log.info("新增用户：用户名重复,请修改后重试！");
            return false;
        }
        UUID userId = UUID.randomUUID();
        user.setUserId(userId.toString());
        int insert = mapper.insert(user);
        return insert > 0;
    }

    /**
     * 根据用户唯一UUID实现删除
     *
     * @param userId 用户的唯一UUID
     * @return
     */
    @Override
    public Boolean deleteByUserId(String userId) {
        UserEntity userEntity = this.getUserByUserId(userId);
        if (userEntity == null) {
            log.info("删除用户：未查询到用户，请核对后再尝试！");
            return false;
        }
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        // 根据用户 ID 进行删除
        wrapper.eq("user_id", userId);
        int delete = mapper.delete(wrapper);

        return delete > 0;
    }

    //endregion 用户数据表操作


    /**
     * 将数据库实体对象转换为用户数据传输对象。
     *
     * @param entity
     * @return
     */
    private UserDTO entityConvertToDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserDTO.builder()
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .userType(entity.getUserType())
                .mqttUsername(entity.getMqttUsername())
                .mqttPassword(entity.getMqttPassword())
                .mqttAddr(MqttPropertyConfiguration.getBasicMqttAddress())
                .build();
    }

    /**
     * 将数据库实体对象转换为用户数据传输对象。
     *
     * @param entity
     * @return
     */
    private UserListDTO entity2UserListDTO(UserEntity entity) {
        UserListDTO.UserListDTOBuilder builder = UserListDTO.builder();
        if (entity != null) {
            builder.userId(entity.getUserId())
                    .username(entity.getUsername())
                    .mqttUsername(entity.getMqttUsername())
                    .mqttPassword(entity.getMqttPassword())
                    .userType(UserTypeEnum.find(entity.getUserType()).getDesc())
                    .createTime(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(entity.getCreateTime()), ZoneId.systemDefault()));
            Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(entity.getWorkspaceId());
            workspaceOpt.ifPresent(workspace -> builder.workspaceName(workspace.getWorkspaceName()));
        }
        return builder.build();
    }
}
