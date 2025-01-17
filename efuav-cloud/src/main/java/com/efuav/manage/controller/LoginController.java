package com.efuav.manage.controller;

import com.efuav.common.error.CommonErrorEnum;
import com.efuav.manage.model.dto.UserDTO;
import com.efuav.manage.model.dto.UserLoginDTO;
import com.efuav.manage.service.IUserService;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.efuav.component.AuthInterceptor.PARAM_TOKEN;

/**
 * 用户登录控制器
 */
@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}")
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 平台登录接口
     *
     * @param loginDTO 用户登录对象类型定义
     * @return 登录成功返回带信息的用户登录对象类型定义
     */
    @PostMapping("/login")
    public HttpResultResponse login(@RequestBody UserLoginDTO loginDTO) {

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        return userService.userLogin(username, password, loginDTO.getFlag());
    }

    /**
     * token恢复接口
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/token/refresh")
    public HttpResultResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(PARAM_TOKEN);
        Optional<UserDTO> user = userService.refreshToken(token);

        if (user.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return HttpResultResponse.error(CommonErrorEnum.NO_TOKEN.getMessage());
        }

        return HttpResultResponse.success(user.get());
    }



}
