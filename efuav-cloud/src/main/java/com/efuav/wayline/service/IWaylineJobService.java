package com.efuav.wayline.service;

import com.efuav.wayline.model.dto.WaylineJobDTO;
import com.efuav.wayline.model.enums.WaylineJobStatusEnum;
import com.efuav.wayline.model.param.CreateJobParam;
import com.efuav.sdk.common.PaginationData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public interface IWaylineJobService {

    /**
     * 在数据库中创建航线作业。
     *
     * @param param
     * @param workspaceId user info
     * @param username    user info
     * @param beginTime   The time the job started.
     * @param endTime     The time the job ended.
     * @return
     */
    Optional<WaylineJobDTO> createWaylineJob(CreateJobParam param, String workspaceId, String username, Long beginTime, Long endTime);

    /**
     * 根据父任务的信息创建子任务。
     *
     * @param workspaceId
     * @param parentId
     * @return
     */
    Optional<WaylineJobDTO> createWaylineJobByParent(String workspaceId, String parentId);

    /**
     * 根据条件查询航线作业。
     *
     * @param workspaceId
     * @param jobIds
     * @param status
     * @return
     */
    List<WaylineJobDTO> getJobsByConditions(String workspaceId, Collection<String> jobIds, WaylineJobStatusEnum status);

    /**
     * 根据作业id查询作业信息。
     *
     * @param workspaceId
     * @param jobId
     * @return job information
     */
    Optional<WaylineJobDTO> getJobByJobId(String workspaceId, String jobId);

    /**
     * 更新作业数据。
     *
     * @param dto
     * @return
     */
    Boolean updateJob(WaylineJobDTO dto);

    /**
     * 浏览此工作区中的所有作业。
     *
     * @param workspaceId
     * @param page
     * @param pageSize
     * @return
     */
    PaginationData<WaylineJobDTO> getJobsByWorkspaceId(String workspaceId, long page, long pageSize);

    /**
     * 查询dock的航线执行状态。
     *
     * @param dockSn
     * @return
     */
    WaylineJobStatusEnum getWaylineState(String dockSn);
}
