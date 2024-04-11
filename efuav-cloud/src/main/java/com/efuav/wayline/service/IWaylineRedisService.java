package com.efuav.wayline.service;

import com.efuav.component.mqtt.model.EventsReceiver;
import com.efuav.wayline.model.dto.ConditionalWaylineJobKey;
import com.efuav.wayline.model.dto.WaylineJobDTO;
import com.efuav.sdk.cloudapi.wayline.FlighttaskProgress;

import java.util.Optional;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/24
 */
public interface IWaylineRedisService {

    /**
     * 将停靠执行的航线作业的状态保存到redis中。
     *
     * @param dockSn
     * @param data
     */
    void setRunningWaylineJob(String dockSn, EventsReceiver<FlighttaskProgress> data);

    /**
     * 在redis中查询dock执行的航线作业的状态。
     *
     * @param dockSn
     * @return
     */
    Optional<EventsReceiver<FlighttaskProgress>> getRunningWaylineJob(String dockSn);

    /**
     * 在redis中删除停靠操作的航线作业状态。
     *
     * @param dockSn
     * @return
     */
    Boolean delRunningWaylineJob(String dockSn);

    /**
     * 将停靠站挂起的航线作业保存到redis。
     *
     * @param dockSn
     * @param jobId
     */
    void setPausedWaylineJob(String dockSn, String jobId);

    /**
     * 在redis中查询dock挂起的航线作业id。
     *
     * @param dockSn
     * @return
     */
    String getPausedWaylineJobId(String dockSn);

    /**
     * 删除由redis中的停靠站挂起的航线作业。
     *
     * @param dockSn
     * @return
     */
    Boolean delPausedWaylineJob(String dockSn);

    /**
     * 将停靠站阻止的航线作业保存到redis。
     *
     * @param dockSn
     * @param jobId
     */
    void setBlockedWaylineJob(String dockSn, String jobId);

    /**
     * 在redis中查询dock阻止的航线作业id。
     *
     * @param dockSn
     * @return
     */
    String getBlockedWaylineJobId(String dockSn);

    /**
     * 通过停靠将条件航线作业保存到redis。
     *
     * @param waylineJob
     */
    void setConditionalWaylineJob(WaylineJobDTO waylineJob);

    /**
     * 通过redis中的dock查询有条件的航线作业id。
     *
     * @param jobId
     * @return
     */
    Optional<WaylineJobDTO> getConditionalWaylineJob(String jobId);

    /**
     * 通过redis中的停靠删除有条件的航线作业。
     *
     * @param jobId
     * @return
     */
    Boolean delConditionalWaylineJob(String jobId);

    Boolean addPrepareConditionalWaylineJob(WaylineJobDTO waylineJob);

    Optional<ConditionalWaylineJobKey> getNearestConditionalWaylineJob();

    Double getConditionalWaylineJobTime(ConditionalWaylineJobKey jobKey);

    Boolean removePrepareConditionalWaylineJob(ConditionalWaylineJobKey jobKey);
}
