package com.efuav.wayline.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.wayline.model.dto.WaylineJobDTO;
import com.efuav.wayline.model.param.CreateJobParam;
import com.efuav.wayline.model.param.UpdateJobParam;
import com.efuav.wayline.service.IFlightTaskService;
import com.efuav.wayline.service.IWaylineJobService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Set;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@RequestMapping("${url.wayline.prefix}${url.wayline.version}/workspaces")
@RestController
public class WaylineJobController {

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private IFlightTaskService flighttaskService;

    /**
     * 为Dock创建航线线任务。
     *
     * @param request
     * @param param
     * @param workspaceId
     * @return
     * @throws SQLException
     */
    @PostMapping("/{workspace_id}/flight-tasks")
    public HttpResultResponse createJob(HttpServletRequest request, @Valid @RequestBody CreateJobParam param,
                                        @PathVariable(name = "workspace_id") String workspaceId) throws SQLException {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        customClaim.setWorkspaceId(workspaceId);

        return flighttaskService.publishFlightTask(param, customClaim);
    }

    /**
     * 浏览此工作区中的所有作业。
     *
     * @param page
     * @param pageSize
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/jobs")
    public HttpResultResponse<PaginationData<WaylineJobDTO>> getJobs(@RequestParam(defaultValue = "1") Long page,
                                                                     @RequestParam(name = "page_size", defaultValue = "10") Long pageSize,
                                                                     @PathVariable(name = "workspace_id") String workspaceId) {
        PaginationData<WaylineJobDTO> data = waylineJobService.getJobsByWorkspaceId(workspaceId, page, pageSize);
        return HttpResultResponse.success(data);
    }

    /**
     * 发送命令以取消作业。
     *
     * @param jobIds
     * @param workspaceId
     * @return
     * @throws SQLException
     */
    @DeleteMapping("/{workspace_id}/jobs")
    public HttpResultResponse publishCancelJob(@RequestParam(name = "job_id") Set<String> jobIds,
                                               @PathVariable(name = "workspace_id") String workspaceId) throws SQLException {
        flighttaskService.cancelFlightTask(workspaceId, jobIds);
        return HttpResultResponse.success();
    }

    /**
     * 将此作业的媒体文件设置为立即上载。
     *
     * @param workspaceId
     * @param jobId
     * @return
     */
    @PostMapping("/{workspace_id}/jobs/{job_id}/media-highest")
    public HttpResultResponse uploadMediaHighestPriority(@PathVariable(name = "workspace_id") String workspaceId,
                                                         @PathVariable(name = "job_id") String jobId) {
        flighttaskService.uploadMediaHighestPriority(workspaceId, jobId);
        return HttpResultResponse.success();
    }

    @PutMapping("/{workspace_id}/jobs/{job_id}")
    public HttpResultResponse updateJobStatus(@PathVariable(name = "workspace_id") String workspaceId,
                                              @PathVariable(name = "job_id") String jobId,
                                              @Valid @RequestBody UpdateJobParam param) {
        flighttaskService.updateJobStatus(workspaceId, jobId, param);
        return HttpResultResponse.success();
    }
}
