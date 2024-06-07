package com.efuav.component;

import com.efuav.common.error.CommonErrorEnum;
import com.efuav.common.model.CustomClaim;
import com.efuav.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String PARAM_TOKEN = "x-auth-token";

    public static final String TOKEN_CLAIM = "customClaim";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.debug("请求URI: {}, IP: {}", uri, request.getRemoteAddr());
        // options方法是直接传递的。
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }
        String token = request.getHeader(PARAM_TOKEN);
        // 检查令牌是否存在。
        if (!StringUtils.hasText(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error(CommonErrorEnum.NO_TOKEN.getMessage());
            return false;
        }

        // 检查当前令牌是否有效。
        Optional<CustomClaim> customClaimOpt = JwtUtil.parseToken(token);
        if (customClaimOpt.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 将令牌中的自定义数据放入请求中。
        request.setAttribute(TOKEN_CLAIM, customClaimOpt.get());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求结束后删除请求中的自定义数据。
        request.removeAttribute(TOKEN_CLAIM);
    }
}
