package com.efuav.control.service.impl;

import com.efuav.component.mqtt.config.MqttPropertyConfiguration;
import com.efuav.component.mqtt.model.EventsReceiver;
import com.efuav.component.mqtt.model.MapKeyConst;
import com.efuav.component.redis.RedisConst;
import com.efuav.component.redis.RedisOpsUtils;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.control.model.dto.JwtAclDTO;
import com.efuav.control.model.enums.DroneAuthorityEnum;
import com.efuav.control.model.enums.MqttAclAccessEnum;
import com.efuav.control.model.param.DrcConnectParam;
import com.efuav.control.model.param.DrcModeParam;
import com.efuav.control.service.IControlService;
import com.efuav.control.service.IDrcService;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.manage.service.IDeviceService;
import com.efuav.wayline.model.enums.WaylineJobStatusEnum;
import com.efuav.wayline.model.enums.WaylineTaskStatusEnum;
import com.efuav.wayline.model.param.UpdateJobParam;
import com.efuav.wayline.service.IFlightTaskService;
import com.efuav.wayline.service.IWaylineJobService;
import com.efuav.wayline.service.IWaylineRedisService;
import com.efuav.sdk.cloudapi.control.DrcModeEnterRequest;
import com.efuav.sdk.cloudapi.control.DrcModeMqttBroker;
import com.efuav.sdk.cloudapi.control.api.AbstractControlService;
import com.efuav.sdk.cloudapi.device.DockModeCodeEnum;
import com.efuav.sdk.cloudapi.device.OsdDockDrone;
import com.efuav.sdk.cloudapi.wayline.FlighttaskProgress;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.SDKManager;
import com.efuav.sdk.mqtt.TopicConst;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
@Service
@Slf4j
public class DrcServiceImpl implements IDrcService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IWaylineJobService waylineJobService;

    @Autowired
    private IFlightTaskService flighttaskService;

    @Autowired
    private IDeviceService deviceService;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private IControlService controlService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private IWaylineRedisService waylineRedisService;

    @Autowired
    private AbstractControlService abstractControlService;

    @Override
    public void setDrcModeInRedis(String dockSn, String clientId) {
        RedisOpsUtils.setWithExpire(RedisConst.DRC_PREFIX + dockSn, clientId, RedisConst.DRC_MODE_ALIVE_SECOND);
    }

    @Override
    public String getDrcModeInRedis(String dockSn) {
        return (String) RedisOpsUtils.get(RedisConst.DRC_PREFIX + dockSn);
    }

    @Override
    public Boolean delDrcModeInRedis(String dockSn) {
        return RedisOpsUtils.del(RedisConst.DRC_PREFIX + dockSn);
    }

    @Override
    public DrcModeMqttBroker userDrcAuth(String workspaceId, String userId, String username, DrcConnectParam param) {

        // 刷新令牌
        String clientId = param.getClientId();
        // 第一次
        if (!StringUtils.hasText(clientId) || !RedisOpsUtils.checkExist(RedisConst.MQTT_ACL_PREFIX + clientId)) {
            clientId = userId + "-" + System.currentTimeMillis();
            RedisOpsUtils.hashSet(RedisConst.MQTT_ACL_PREFIX + clientId, "", MqttAclAccessEnum.ALL.getValue());
        }

        String key = RedisConst.MQTT_ACL_PREFIX + clientId;

        try {
            RedisOpsUtils.expireKey(key, RedisConst.DRC_MODE_ALIVE_SECOND);

            return MqttPropertyConfiguration.getMqttBrokerWithDrc(
                    clientId, username, param.getExpireSec(), Collections.emptyMap());
        } catch (RuntimeException e) {
            RedisOpsUtils.del(key);
            throw e;
        }
    }

    private void checkDrcModeCondition(String workspaceId, String dockSn) {
        Optional<EventsReceiver<FlighttaskProgress>> runningOpt = waylineRedisService.getRunningWaylineJob(dockSn);
        if (runningOpt.isPresent() && WaylineJobStatusEnum.IN_PROGRESS == waylineJobService.getWaylineState(dockSn)) {
            flighttaskService.updateJobStatus(workspaceId, runningOpt.get().getBid(),
                    UpdateJobParam.builder().status(WaylineTaskStatusEnum.PAUSE).build());
        }

        DockModeCodeEnum dockMode = deviceService.getDockMode(dockSn);
        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isPresent() && (DockModeCodeEnum.IDLE == dockMode || DockModeCodeEnum.WORKING == dockMode)) {
            Optional<OsdDockDrone> deviceOsd = deviceRedisService.getDeviceOsd(dockOpt.get().getChildDeviceSn(), OsdDockDrone.class);
            if (deviceOsd.isEmpty() || deviceOsd.get().getElevation() <= 0) {
                throw new RuntimeException("无人机不在空中，无法进入命令飞行模式。");
            }
        } else {
            throw new RuntimeException("机场的当前状态不支持进入命令飞行模式。");
        }

        HttpResultResponse result = controlService.seizeAuthority(dockSn, DroneAuthorityEnum.FLIGHT, null);
        if (HttpResultResponse.CODE_SUCCESS != result.getCode()) {
            throw new IllegalArgumentException(result.getMessage());
        }

    }

    @Override
    public JwtAclDTO deviceDrcEnter(String workspaceId, DrcModeParam param) {
        String topic = TopicConst.THING_MODEL_PRE + TopicConst.PRODUCT + param.getDockSn() + TopicConst.DRC;
        String pubTopic = topic + TopicConst.DOWN;
        String subTopic = topic + TopicConst.UP;

        // 如果dock处于drc模式，则直接刷新权限。
        if (deviceService.checkDockDrcMode(param.getDockSn())
                && param.getClientId().equals(this.getDrcModeInRedis(param.getDockSn()))) {
            refreshAcl(param.getDockSn(), param.getClientId(), topic, subTopic);
            return JwtAclDTO.builder().sub(List.of(subTopic)).pub(List.of(pubTopic)).build();
        }

        checkDrcModeCondition(workspaceId, param.getDockSn());

        TopicServicesResponse<ServicesReplyData> reply = abstractControlService.drcModeEnter(
                SDKManager.getDeviceSDK(param.getDockSn()),
                new DrcModeEnterRequest()
                        .setMqttBroker(MqttPropertyConfiguration.getMqttBrokerWithDrc(param.getDockSn() + "-" + System.currentTimeMillis(), param.getDockSn(),
                                RedisConst.DRC_MODE_ALIVE_SECOND.longValue(),
                                Map.of(MapKeyConst.ACL, objectMapper.convertValue(JwtAclDTO.builder()
                                        .pub(List.of(subTopic))
                                        .sub(List.of(pubTopic))
                                        .build(), new TypeReference<Map<String, ?>>() {}))))
                        .setHsiFrequency(1).setOsdFrequency(10));

        if (!reply.getData().getResult().isSuccess()) {
            throw new RuntimeException("SN: " + param.getDockSn() + "; Error:" + reply.getData().getResult() +
                    "; 无法进入指令飞行控制模式，请稍后再试！");
        }

        refreshAcl(param.getDockSn(), param.getClientId(), pubTopic, subTopic);
        return JwtAclDTO.builder().sub(List.of(subTopic)).pub(List.of(pubTopic)).build();
    }

    private void refreshAcl(String dockSn, String clientId, String pubTopic, String subTopic) {
        this.setDrcModeInRedis(dockSn, clientId);

        // assign acl，Match by clientId. https://www.emqx.io/docs/zh/v4.4/advanced/acl-redis.html
        // scheme: HSET mqtt_acl:[clientid] [topic] [access]
        String key = RedisConst.MQTT_ACL_PREFIX + clientId;
        RedisOpsUtils.hashSet(key, pubTopic, MqttAclAccessEnum.PUB.getValue());
        RedisOpsUtils.hashSet(key, subTopic, MqttAclAccessEnum.SUB.getValue());
        RedisOpsUtils.expireKey(key, RedisConst.DRC_MODE_ALIVE_SECOND);
    }

    @Override
    public void deviceDrcExit(String workspaceId, DrcModeParam param) {
        if (!deviceService.checkDockDrcMode(param.getDockSn())) {
            throw new RuntimeException("机场未处于飞行控制模式。");
        }
        TopicServicesResponse<ServicesReplyData> reply =
                abstractControlService.drcModeExit(SDKManager.getDeviceSDK(param.getDockSn()));
        if (!reply.getData().getResult().isSuccess()) {
            throw new RuntimeException("SN: " + param.getDockSn() + "; Error:" +
                    reply.getData().getResult() + "; 退出指令飞行控制模式失败，请稍后再试！");
        }

        String jobId = waylineRedisService.getPausedWaylineJobId(param.getDockSn());
        if (StringUtils.hasText(jobId)) {
            flighttaskService.updateJobStatus(workspaceId, jobId, UpdateJobParam.builder().status(WaylineTaskStatusEnum.RESUME).build());
        }

        this.delDrcModeInRedis(param.getDockSn());
        RedisOpsUtils.del(RedisConst.MQTT_ACL_PREFIX + param.getClientId());
    }

}
