package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.WorkspaceDTO;
import com.efuav.manage.model.entity.WorkspaceEntity;
import com.efuav.manage.service.IWorkspaceService;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
     * 获取有关工作区的信息。
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public HttpResultResponse getCurrentWorkspace(HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        //对比账户UUID，若是系统管理员，则返回所有工作区信息
        if ("d14a3689-98e8-4c9f-b839-962056555149".equals(customClaim.getId())) {
            List<WorkspaceDTO> allWorkspaces = workspaceService.getAllWorkspace();
            return allWorkspaces == null || allWorkspaces.isEmpty() ? HttpResultResponse.error() : HttpResultResponse.success(allWorkspaces);
        }
        //否则，返回当前用户所属的工作区信息
        Optional<WorkspaceDTO> workspaceOpt = workspaceService.getWorkspaceByWorkspaceId(customClaim.getWorkspaceId());
        return workspaceOpt.isEmpty() ? HttpResultResponse.error() : HttpResultResponse.success(workspaceOpt.get());
    }

    /**
     * 新建工作空间
     *
     * @param workspaceEntity 工作空间对象
     * @param request
     * @return
     */
    @PostMapping("/insertWorkSpace")
    public HttpResultResponse insertWorkSpace(@RequestBody WorkspaceEntity workspaceEntity, HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        if (customClaim.getUserType() != 2) {
            boolean result = workspaceService.insertWorkSpace(workspaceEntity);
            return result ? HttpResultResponse.success("新建工作空间成功！") : HttpResultResponse.error("新建工作空间失败，请检查参数后重新尝试！");
        }
        return HttpResultResponse.error("您暂无新建工作空间的权限！");
    }

    /**
     * 删除工作空间 未实现
     *
     * @param workspaceId 工作空间对象id
     * @param request
     * @return
     */
//    @DeleteMapping("/deleteWorkSpace")
//    public HttpResultResponse deleteWorkSpace(@RequestParam String workspaceId, HttpServletRequest request) {
//        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
//        if ("d14a3689-98e8-4c9f-b839-962056555149".equals(customClaim.getId())) {
//            boolean result = workspaceService.deleteWorkSpace(workspaceId);
//            return result ? HttpResultResponse.success("删除工作空间成功！") : HttpResultResponse.error("删除工作空间失败，请联系管理员！");
//        }
//        return HttpResultResponse.error("您暂无删除工作空间的权限！");
//    }

    /**
     * 修改工作空间信息
     *
     * @param workspaceDTO
     * @param request
     * @return
     */
    @PutMapping("/updateWorkSpace")
    public HttpResultResponse updateWorkSpace(@RequestBody WorkspaceDTO workspaceDTO, HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        if (customClaim.getUserType() != 2) {
            boolean result = workspaceService.updateWorkSpace(workspaceDTO);
            return result ? HttpResultResponse.success("修改工作空间成功！") : HttpResultResponse.error("修改工作空间失败，请联系管理员！");
        }
        return HttpResultResponse.error("您暂无修改工作空间的权限！");
    }
}