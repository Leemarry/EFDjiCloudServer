package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.WorkspaceDTO;
import com.efuav.manage.service.IWorkspaceService;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/23
 */
@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
public class WorkspaceController {

    @Autowired
    private IWorkspaceService workspaceService;

    /**
     * 获取有关当前用户所在的工作区的信息。
     * @param request
     * @return
     */
    @GetMapping("/current")
    public HttpResultResponse getCurrentWorkspace(HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);
        Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(customClaim.getWorkspaceId());

        return workspaceOpt.isEmpty() ? HttpResultResponse.error() : HttpResultResponse.success(workspaceOpt.get());
    }
}