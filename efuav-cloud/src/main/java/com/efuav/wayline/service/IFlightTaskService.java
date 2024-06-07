package com.efuav.wayline.service;

import com.efuav.common.model.CustomClaim;
import com.efuav.wayline.model.dto.ConditionalWaylineJobKey;
import com.efuav.wayline.model.dto.WaylineJobDTO;
import com.efuav.wayline.model.param.CreateJobParam;
import com.efuav.wayline.model.param.UpdateJobParam;
import com.efuav.sdk.common.HttpResultResponse;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
public interface IFlightTaskService {


    /**
     * 向dock发出航线任务。
     *
     * @param param
     * @param customClaim 用户信息
     * @return
     */
    HttpResultResponse publishFlightTask(CreateJobParam param, CustomClaim customClaim) throws SQLException;

    /**
     * 向dock发出航线任务。
     *
     * @param waylineJob
     * @return
     * @throws SQLException
     */
    HttpResultResponse publishOneFlightTask(WaylineJobDTO waylineJob) throws SQLException;

    /**
     * 立即执行任务。
     *
     * @param jobId
     * @return
     * @throws SQLException
     */
    Boolean executeFlightTask(String workspaceId, String jobId);

    /**
     * 根据作业ID取消任务。
     *
     * @param workspaceId
     * @param jobIds
     * @throws SQLException
     */
    void cancelFlightTask(String workspaceId, Collection<String> jobIds);

    /**
     * 取消已发布但尚未执行的停靠任务。
     *
     * @param workspaceId
     * @param dockSn
     * @param jobIds
     */
    void publishCancelTask(String workspaceId, String dockSn, List<String> jobIds);

    /**
     * 将此作业的媒体文件设置为立即上传。
     *
     * @param workspaceId
     * @param jobId
     */
    void uploadMediaHighestPriority(String workspaceId, String jobId);

    /**
     * 手动控制航线作业的执行状态。
     *
     * @param workspaceId
     * @param jobId
     * @param param
     */
    void updateJobStatus(String workspaceId, String jobId, UpdateJobParam param);

    void retryPrepareJob(ConditionalWaylineJobKey jobKey, WaylineJobDTO waylineJob);
}
