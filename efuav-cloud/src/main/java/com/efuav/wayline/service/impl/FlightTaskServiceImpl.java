package com.efuav.wayline.service.impl;

import com.efuav.common.error.CommonErrorEnum;
import com.efuav.common.model.CustomClaim;
import com.efuav.component.mqtt.model.EventsReceiver;
import com.efuav.component.redis.RedisConst;
import com.efuav.component.redis.RedisOpsUtils;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.media.model.MediaFileCountDTO;
import com.efuav.media.service.IMediaRedisService;
import com.efuav.wayline.model.dto.ConditionalWaylineJobKey;
import com.efuav.wayline.model.dto.WaylineJobDTO;
import com.efuav.wayline.model.dto.WaylineTaskConditionDTO;
import com.efuav.wayline.model.enums.WaylineErrorCodeEnum;
import com.efuav.wayline.model.enums.WaylineJobStatusEnum;
import com.efuav.wayline.model.param.CreateJobParam;
import com.efuav.wayline.model.param.UpdateJobParam;
import com.efuav.wayline.service.IFlightTaskService;
import com.efuav.wayline.service.IWaylineFileService;
import com.efuav.wayline.service.IWaylineJobService;
import com.efuav.wayline.service.IWaylineRedisService;
import com.efuav.sdk.cloudapi.device.ExitWaylineWhenRcLostEnum;
import com.efuav.sdk.cloudapi.media.UploadFlighttaskMediaPrioritize;
import com.efuav.sdk.cloudapi.media.api.AbstractMediaService;
import com.efuav.sdk.cloudapi.wayline.*;
import com.efuav.sdk.cloudapi.wayline.api.AbstractWaylineService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.SDKManager;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Service
@Slf4j
public class FlightTaskServiceImpl extends AbstractWaylineService implements IFlightTaskService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IWebSocketMessageService websocketMessageService;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private IWaylineRedisService waylineRedisService;

    @Autowired
    private IMediaRedisService mediaRedisService;

    @Autowired
    private IWaylineFileService waylineFileService;

    @Autowired
    private SDKWaylineService abstractWaylineService;

    @Autowired
    @Qualifier("mediaServiceImpl")
    private AbstractMediaService abstractMediaService;

    @Scheduled(initialDelay = 10, fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void checkScheduledJob() {
        Object jobIdValue = RedisOpsUtils.zGetMin(RedisConst.WAYLINE_JOB_TIMED_EXECUTE);
        if (Objects.isNull(jobIdValue)) {
            return;
        }
        log.info("检查航线的定时任务。 {}", jobIdValue);
        // format: {workspace_id}:{dock_sn}:{job_id}
        String[] jobArr = String.valueOf(jobIdValue).split(RedisConst.DELIMITER);
        double time = RedisOpsUtils.zScore(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, jobIdValue);
        long now = System.currentTimeMillis();
        int offset = 30_000;

        // 过期的任务将被直接删除。
        if (time < now - offset) {
            RedisOpsUtils.zRemove(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, jobIdValue);
            waylineJobService.updateJob(WaylineJobDTO.builder()
                    .jobId(jobArr[2])
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .executeTime(LocalDateTime.now())
                    .completedTime(LocalDateTime.now())
                    .code(HttpStatus.SC_REQUEST_TIMEOUT).build());
            return;
        }

        if (now <= time && time <= now + offset) {
            try {
                this.executeFlightTask(jobArr[0], jobArr[2]);
            } catch (Exception e) {
                log.info("计划的任务传递失败。");
                waylineJobService.updateJob(WaylineJobDTO.builder()
                        .jobId(jobArr[2])
                        .status(WaylineJobStatusEnum.FAILED.getVal())
                        .executeTime(LocalDateTime.now())
                        .completedTime(LocalDateTime.now())
                        .code(HttpStatus.SC_INTERNAL_SERVER_ERROR).build());
            } finally {
                RedisOpsUtils.zRemove(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, jobIdValue);
            }
        }
    }

    @Scheduled(initialDelay = 10, fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void prepareConditionJob() {
        Optional<ConditionalWaylineJobKey> jobKeyOpt = waylineRedisService.getNearestConditionalWaylineJob();
        if (jobKeyOpt.isEmpty()) {
            return;
        }
        ConditionalWaylineJobKey jobKey = jobKeyOpt.get();
        log.info("检查航线的条件任务。 {}", jobKey.toString());
        // format: {workspace_id}:{dock_sn}:{job_id}
        double time = waylineRedisService.getConditionalWaylineJobTime(jobKey);
        long now = System.currentTimeMillis();
        // 提前一天准备任务。
        int offset = 86_400_000;

        if (now + offset < time) {
            return;
        }

        WaylineJobDTO job = WaylineJobDTO.builder()
                .jobId(jobKey.getJobId())
                .status(WaylineJobStatusEnum.FAILED.getVal())
                .executeTime(LocalDateTime.now())
                .completedTime(LocalDateTime.now())
                .code(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        try {
            Optional<WaylineJobDTO> waylineJobOpt = waylineRedisService.getConditionalWaylineJob(jobKey.getJobId());
            if (waylineJobOpt.isEmpty()) {
                job.setCode(CommonErrorEnum.REDIS_DATA_NOT_FOUND.getCode());
                waylineJobService.updateJob(job);
                waylineRedisService.removePrepareConditionalWaylineJob(jobKey);
                return;
            }
            WaylineJobDTO waylineJob = waylineJobOpt.get();

            HttpResultResponse result = this.publishOneFlightTask(waylineJob);
            waylineRedisService.removePrepareConditionalWaylineJob(jobKey);

            if (HttpResultResponse.CODE_SUCCESS == result.getCode()) {
                return;
            }

            // 如果超过结束时间，将不再重试。
            waylineRedisService.delConditionalWaylineJob(jobKey.getJobId());
            if (waylineJob.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - RedisConst.WAYLINE_JOB_BLOCK_TIME * 1000 < now) {
                return;
            }

            // 如果没有超过结束时间，请重试。
            this.retryPrepareJob(jobKey, waylineJob);

        } catch (Exception e) {
            log.info("无法准备条件任务。");
            waylineJobService.updateJob(job);
        }

    }

    /**
     * 对于即时任务，以服务器时间为准。
     *
     * @param param
     */
    private void fillImmediateTime(CreateJobParam param) {
        if (TaskTypeEnum.IMMEDIATE != param.getTaskType()) {
            return;
        }
        long now = System.currentTimeMillis() / 1000;
        param.setTaskDays(List.of(now));
        param.setTaskPeriods(List.of(List.of(now)));
    }


    private void addConditions(WaylineJobDTO waylineJob, CreateJobParam param, Long beginTime, Long endTime) {
        if (TaskTypeEnum.CONDITIONAL != param.getTaskType()) {
            return;
        }

        waylineJob.setConditions(
                WaylineTaskConditionDTO.builder()
                        .executableConditions(Objects.nonNull(param.getMinStorageCapacity()) ?
                                new ExecutableConditions().setStorageCapacity(param.getMinStorageCapacity()) : null)
                        .readyConditions(new ReadyConditions()
                                .setBatteryCapacity(param.getMinBatteryCapacity())
                                .setBeginTime(beginTime)
                                .setEndTime(endTime))
                        .build());

        waylineRedisService.setConditionalWaylineJob(waylineJob);
        // key: wayline_job_condition, value: {workspace_id}:{dock_sn}:{job_id}
        boolean isAdd = waylineRedisService.addPrepareConditionalWaylineJob(waylineJob);
        if (!isAdd) {
            throw new RuntimeException("无法创建有条件的作业。");
        }
    }

    @Override
    public HttpResultResponse publishFlightTask(CreateJobParam param, CustomClaim customClaim) throws SQLException {
        fillImmediateTime(param);

        for (Long taskDay : param.getTaskDays()) {
            LocalDate date = LocalDate.ofInstant(Instant.ofEpochSecond(taskDay), ZoneId.systemDefault());
            for (List<Long> taskPeriod : param.getTaskPeriods()) {
                long beginTime = LocalDateTime.of(date, LocalTime.ofInstant(Instant.ofEpochSecond(taskPeriod.get(0)), ZoneId.systemDefault()))
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endTime = taskPeriod.size() > 1 ?
                        LocalDateTime.of(date, LocalTime.ofInstant(Instant.ofEpochSecond(taskPeriod.get(1)), ZoneId.systemDefault()))
                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : beginTime;
                if (TaskTypeEnum.IMMEDIATE != param.getTaskType() && endTime < System.currentTimeMillis()) {
                    continue;
                }

                Optional<WaylineJobDTO> waylineJobOpt = waylineJobService.createWaylineJob(param, customClaim.getWorkspaceId(), customClaim.getUsername(), beginTime, endTime);
                if (waylineJobOpt.isEmpty()) {
                    throw new SQLException("无法创建航线作业。");
                }
                WaylineJobDTO waylineJob = waylineJobOpt.get();
                // 如果是条件任务类型，请将条件添加到航线作业任务参数中。
                addConditions(waylineJob, param, beginTime, endTime);

                HttpResultResponse response = this.publishOneFlightTask(waylineJob);
                if (HttpResultResponse.CODE_SUCCESS != response.getCode()) {
                    return response;
                }
            }
        }
        return HttpResultResponse.success();
    }

    @Override
    public HttpResultResponse publishOneFlightTask(WaylineJobDTO waylineJob) throws SQLException {

        boolean isOnline = deviceRedisService.checkDeviceOnline(waylineJob.getDockSn());
        if (!isOnline) {
            throw new RuntimeException("机场处于离线状态.");
        }

        boolean isSuccess = this.prepareFlightTask(waylineJob);
        if (!isSuccess) {
            return HttpResultResponse.error("无法准备航线作业任务。");
        }

        // 发出立即任务执行命令。
        if (TaskTypeEnum.IMMEDIATE == waylineJob.getTaskType()) {
            if (!executeFlightTask(waylineJob.getWorkspaceId(), waylineJob.getJobId())) {
                return HttpResultResponse.error("无法执行航线作业任务。");
            }
        }

        if (TaskTypeEnum.TIMED == waylineJob.getTaskType()) {
            // key: wayline_job_timed, value: {workspace_id}:{dock_sn}:{job_id}
            boolean isAdd = RedisOpsUtils.zAdd(RedisConst.WAYLINE_JOB_TIMED_EXECUTE,
                    waylineJob.getWorkspaceId() + RedisConst.DELIMITER + waylineJob.getDockSn() + RedisConst.DELIMITER + waylineJob.getJobId(),
                    waylineJob.getBeginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            if (!isAdd) {
                return HttpResultResponse.error("无法创建计划航线作业任务。");
            }
        }

        return HttpResultResponse.success();
    }

    private Boolean prepareFlightTask(WaylineJobDTO waylineJob) throws SQLException {
        // 获取航线文件
        Optional<GetWaylineListResponse> waylineFile = waylineFileService.getWaylineByWaylineId(waylineJob.getWorkspaceId(), waylineJob.getFileId());
        if (waylineFile.isEmpty()) {
            throw new SQLException("航线文件不存在。");
        }

        // 获取文件url
        URL url = waylineFileService.getObjectUrl(waylineJob.getWorkspaceId(), waylineFile.get().getId());

        FlighttaskPrepareRequest flightTask = new FlighttaskPrepareRequest()
                .setFlightId(waylineJob.getJobId())
                .setExecuteTime(waylineJob.getBeginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setTaskType(waylineJob.getTaskType())
                .setWaylineType(waylineJob.getWaylineType())
                .setRthAltitude(waylineJob.getRthAltitude())
                .setOutOfControlAction(waylineJob.getOutOfControlAction())
                .setExitWaylineWhenRcLost(ExitWaylineWhenRcLostEnum.EXECUTE_RC_LOST_ACTION)
                .setFile(new FlighttaskFile()
                        .setUrl(url.toString())
                        .setFingerprint(waylineFile.get().getSign()));

        if (TaskTypeEnum.CONDITIONAL == waylineJob.getTaskType()) {
            if (Objects.isNull(waylineJob.getConditions())) {
                throw new IllegalArgumentException();
            }
            flightTask.setReadyConditions(waylineJob.getConditions().getReadyConditions());
            flightTask.setExecutableConditions(waylineJob.getConditions().getExecutableConditions());
        }

        TopicServicesResponse<ServicesReplyData> serviceReply = abstractWaylineService.flighttaskPrepare(
                SDKManager.getDeviceSDK(waylineJob.getDockSn()), flightTask);
        if (!serviceReply.getData().getResult().isSuccess()) {
            log.info("准备任务 ====> Error code: {}", serviceReply.getData().getResult());
            waylineJobService.updateJob(WaylineJobDTO.builder()
                    .workspaceId(waylineJob.getWorkspaceId())
                    .jobId(waylineJob.getJobId())
                    .executeTime(LocalDateTime.now())
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .completedTime(LocalDateTime.now())
                    .code(serviceReply.getData().getResult().getCode()).build());
            return false;
        }
        return true;
    }


    @Override
    public Boolean executeFlightTask(String workspaceId, String jobId) {
        // 得到任务
        Optional<WaylineJobDTO> waylineJob = waylineJobService.getJobByJobId(workspaceId, jobId);
        if (waylineJob.isEmpty()) {
            throw new IllegalArgumentException("航线作业任务不存在。");
        }

        boolean isOnline = deviceRedisService.checkDeviceOnline(waylineJob.get().getDockSn());
        if (!isOnline) {
            throw new RuntimeException("机场处于离线状态。");
        }

        WaylineJobDTO job = waylineJob.get();

        TopicServicesResponse<ServicesReplyData> serviceReply = abstractWaylineService.flighttaskExecute(
                SDKManager.getDeviceSDK(job.getDockSn()), new FlighttaskExecuteRequest().setFlightId(jobId));
        if (!serviceReply.getData().getResult().isSuccess()) {
            log.info("执行航线作业任务 ====> Error: {}", serviceReply.getData().getResult());
            waylineJobService.updateJob(WaylineJobDTO.builder()
                    .jobId(jobId)
                    .executeTime(LocalDateTime.now())
                    .status(WaylineJobStatusEnum.FAILED.getVal())
                    .completedTime(LocalDateTime.now())
                    .code(serviceReply.getData().getResult().getCode()).build());
            // 条件任务失败，进入阻塞状态。
            if (TaskTypeEnum.CONDITIONAL == job.getTaskType()
                    && WaylineErrorCodeEnum.find(serviceReply.getData().getResult().getCode()).isBlock()) {
                waylineRedisService.setBlockedWaylineJob(job.getDockSn(), jobId);
            }
            return false;
        }

        waylineJobService.updateJob(WaylineJobDTO.builder()
                .jobId(jobId)
                .executeTime(LocalDateTime.now())
                .status(WaylineJobStatusEnum.IN_PROGRESS.getVal())
                .build());
        waylineRedisService.setRunningWaylineJob(job.getDockSn(), EventsReceiver.<FlighttaskProgress>builder().bid(jobId).sn(job.getDockSn()).build());
        return true;
    }

    @Override
    public void cancelFlightTask(String workspaceId, Collection<String> jobIds) {
        List<WaylineJobDTO> waylineJobs = waylineJobService.getJobsByConditions(workspaceId, jobIds, WaylineJobStatusEnum.PENDING);

        Set<String> waylineJobIds = waylineJobs.stream().map(WaylineJobDTO::getJobId).collect(Collectors.toSet());
        // 检查任务状态是否正确。
        boolean isErr = !jobIds.removeAll(waylineJobIds) || !jobIds.isEmpty();
        if (isErr) {
            throw new IllegalArgumentException("这些任务的状态不正确，无法取消。 " + Arrays.toString(jobIds.toArray()));
        }

        // 按dock sn对航线作业任务id进行分组。
        Map<String, List<String>> dockJobs = waylineJobs.stream()
                .collect(Collectors.groupingBy(WaylineJobDTO::getDockSn,
                        Collectors.mapping(WaylineJobDTO::getJobId, Collectors.toList())));
        dockJobs.forEach((dockSn, idList) -> this.publishCancelTask(workspaceId, dockSn, idList));

    }

    public void publishCancelTask(String workspaceId, String dockSn, List<String> jobIds) {
        boolean isOnline = deviceRedisService.checkDeviceOnline(dockSn);
        if (!isOnline) {
            throw new RuntimeException("机场处于离线状态.");
        }

        TopicServicesResponse<ServicesReplyData> serviceReply = abstractWaylineService.flighttaskUndo(SDKManager.getDeviceSDK(dockSn),
                new FlighttaskUndoRequest().setFlightIds(jobIds));
        if (!serviceReply.getData().getResult().isSuccess()) {
            log.info("取消航线作业任务 ====> Error: {}", serviceReply.getData().getResult());
            throw new RuntimeException("未能取消的航线航线作业任务 " + dockSn);
        }

        for (String jobId : jobIds) {
            waylineJobService.updateJob(WaylineJobDTO.builder()
                    .workspaceId(workspaceId)
                    .jobId(jobId)
                    .status(WaylineJobStatusEnum.CANCEL.getVal())
                    .completedTime(LocalDateTime.now())
                    .build());
            RedisOpsUtils.zRemove(RedisConst.WAYLINE_JOB_TIMED_EXECUTE, workspaceId + RedisConst.DELIMITER + dockSn + RedisConst.DELIMITER + jobId);
        }

    }

    @Override
    public void uploadMediaHighestPriority(String workspaceId, String jobId) {
        Optional<WaylineJobDTO> jobOpt = waylineJobService.getJobByJobId(workspaceId, jobId);
        if (jobOpt.isEmpty()) {
            throw new RuntimeException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
        }

        String dockSn = jobOpt.get().getDockSn();
        String key = RedisConst.MEDIA_HIGHEST_PRIORITY_PREFIX + dockSn;
        if (RedisOpsUtils.checkExist(key) && jobId.equals(((MediaFileCountDTO) RedisOpsUtils.get(key)).getJobId())) {
            return;
        }

        TopicServicesResponse<ServicesReplyData> reply = abstractMediaService.uploadFlighttaskMediaPrioritize(
                SDKManager.getDeviceSDK(dockSn), new UploadFlighttaskMediaPrioritize().setFlightId(jobId));
        if (!reply.getData().getResult().isSuccess()) {
            throw new RuntimeException("无法设置媒体作业上传优先级。 Error: " + reply.getData().getResult());
        }
    }

    @Override
    public void updateJobStatus(String workspaceId, String jobId, UpdateJobParam param) {
        Optional<WaylineJobDTO> waylineJobOpt = waylineJobService.getJobByJobId(workspaceId, jobId);
        if (waylineJobOpt.isEmpty()) {
            throw new RuntimeException("航线作业任务不存在。");
        }
        WaylineJobDTO waylineJob = waylineJobOpt.get();
        WaylineJobStatusEnum statusEnum = waylineJobService.getWaylineState(waylineJob.getDockSn());
        if (statusEnum.getEnd() || WaylineJobStatusEnum.PENDING == statusEnum) {
            throw new RuntimeException("航线作业任务状态不匹配，因此无法执行该操作。");
        }

        switch (param.getStatus()) {
            case PAUSE:
                pauseJob(workspaceId, waylineJob.getDockSn(), jobId, statusEnum);
                break;
            case RESUME:
                resumeJob(workspaceId, waylineJob.getDockSn(), jobId, statusEnum);
                break;
        }

    }

    private void pauseJob(String workspaceId, String dockSn, String jobId, WaylineJobStatusEnum statusEnum) {
        if (WaylineJobStatusEnum.PAUSED == statusEnum && jobId.equals(waylineRedisService.getPausedWaylineJobId(dockSn))) {
            waylineRedisService.setPausedWaylineJob(dockSn, jobId);
            return;
        }

        TopicServicesResponse<ServicesReplyData> reply = abstractWaylineService.flighttaskPause(SDKManager.getDeviceSDK(dockSn));
        if (!reply.getData().getResult().isSuccess()) {
            throw new RuntimeException("无法暂停航线作业任务。 Error: " + reply.getData().getResult());
        }
        waylineRedisService.delRunningWaylineJob(dockSn);
        waylineRedisService.setPausedWaylineJob(dockSn, jobId);
    }

    private void resumeJob(String workspaceId, String dockSn, String jobId, WaylineJobStatusEnum statusEnum) {
        Optional<EventsReceiver<FlighttaskProgress>> runningDataOpt = waylineRedisService.getRunningWaylineJob(dockSn);
        if (WaylineJobStatusEnum.IN_PROGRESS == statusEnum && jobId.equals(runningDataOpt.map(EventsReceiver::getSn).get())) {
            waylineRedisService.setRunningWaylineJob(dockSn, runningDataOpt.get());
            return;
        }
        TopicServicesResponse<ServicesReplyData> reply = abstractWaylineService.flighttaskRecovery(SDKManager.getDeviceSDK(dockSn));
        if (!reply.getData().getResult().isSuccess()) {
            throw new RuntimeException("无法恢复航线作业任务。 Error: " + reply.getData().getResult());
        }

        runningDataOpt.ifPresent(runningData -> waylineRedisService.setRunningWaylineJob(dockSn, runningData));
        waylineRedisService.delPausedWaylineJob(dockSn);
    }

    @Override
    public void retryPrepareJob(ConditionalWaylineJobKey jobKey, WaylineJobDTO waylineJob) {
        Optional<WaylineJobDTO> childJobOpt = waylineJobService.createWaylineJobByParent(jobKey.getWorkspaceId(), jobKey.getJobId());
        if (childJobOpt.isEmpty()) {
            log.error("无法创建航线作业任务。");
            return;
        }

        WaylineJobDTO newJob = childJobOpt.get();
        newJob.setBeginTime(LocalDateTime.now().plusSeconds(RedisConst.WAYLINE_JOB_BLOCK_TIME));
        boolean isAdd = waylineRedisService.addPrepareConditionalWaylineJob(newJob);
        if (!isAdd) {
            log.error("无法创建航线作业任务。 {}", newJob.getJobId());
            return;
        }

        waylineJob.setJobId(newJob.getJobId());
        waylineRedisService.setConditionalWaylineJob(waylineJob);
    }


    @Override
    public TopicEventsResponse<MqttReply> flighttaskReady(TopicEventsRequest<FlighttaskReady> response, MessageHeaders headers) {
        List<String> flightIds = response.getData().getFlightIds();

        log.info("就绪任务列表：{}", Arrays.toString(flightIds.toArray()));
        // 检查条件任务阻止状态。
        String blockedId = waylineRedisService.getBlockedWaylineJobId(response.getGateway());
        if (!StringUtils.hasText(blockedId)) {
            return null;
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(response.getGateway());
        if (deviceOpt.isEmpty()) {
            return null;
        }
        DeviceDTO device = deviceOpt.get();

        try {
            for (String jobId : flightIds) {
                boolean isExecute = this.executeFlightTask(device.getWorkspaceId(), jobId);
                if (!isExecute) {
                    return null;
                }
                Optional<WaylineJobDTO> waylineJobOpt = waylineRedisService.getConditionalWaylineJob(jobId);
                if (waylineJobOpt.isEmpty()) {
                    log.info("条件航线作业任务已过期，将不再执行。");
                    return new TopicEventsResponse<>();
                }
                WaylineJobDTO waylineJob = waylineJobOpt.get();
                this.retryPrepareJob(new ConditionalWaylineJobKey(device.getWorkspaceId(), response.getGateway(), jobId), waylineJob);
                return new TopicEventsResponse<>();
            }
        } catch (Exception e) {
            log.error("未能执行航线条件任务。");
            e.printStackTrace();
        }
        return new TopicEventsResponse<>();
    }

}
