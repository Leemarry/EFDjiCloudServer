package com.efuav.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efuav.common.error.CommonErrorEnum;
import com.efuav.component.mqtt.model.EventsReceiver;
import com.efuav.component.websocket.model.BizCodeEnum;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.control.model.enums.DroneAuthorityEnum;
import com.efuav.manage.dao.IDeviceMapper;
import com.efuav.manage.model.dto.*;
import com.efuav.manage.model.entity.DeviceEntity;
import com.efuav.manage.model.enums.DeviceFirmwareStatusEnum;
import com.efuav.manage.model.enums.PropertySetFieldEnum;
import com.efuav.manage.model.enums.UserTypeEnum;
import com.efuav.manage.model.param.DeviceQueryParam;
import com.efuav.manage.model.receiver.BasicDeviceProperty;
import com.efuav.manage.service.*;
import com.efuav.sdk.cloudapi.device.*;
import com.efuav.sdk.cloudapi.firmware.*;
import com.efuav.sdk.cloudapi.firmware.api.AbstractFirmwareService;
import com.efuav.sdk.cloudapi.property.api.AbstractPropertyService;
import com.efuav.sdk.cloudapi.tsa.DeviceIconUrl;
import com.efuav.sdk.cloudapi.tsa.TopologyDeviceModel;
import com.efuav.sdk.common.*;
import com.efuav.sdk.config.version.GatewayManager;
import com.efuav.sdk.exception.CloudSDKException;
import com.efuav.sdk.mqtt.IMqttTopicService;
import com.efuav.sdk.mqtt.MqttGatewayPublish;
import com.efuav.sdk.mqtt.events.EventsSubscribe;
import com.efuav.sdk.mqtt.osd.OsdSubscribe;
import com.efuav.sdk.mqtt.property.PropertySetReplyResultEnum;
import com.efuav.sdk.mqtt.property.PropertySetSubscribe;
import com.efuav.sdk.mqtt.requests.RequestsSubscribe;
import com.efuav.sdk.mqtt.services.ServicesReplyData;
import com.efuav.sdk.mqtt.services.ServicesSubscribe;
import com.efuav.sdk.mqtt.services.TopicServicesResponse;
import com.efuav.sdk.mqtt.state.StateSubscribe;
import com.efuav.sdk.mqtt.status.StatusSubscribe;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/10
 */
@Service
@Slf4j
@Transactional
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    private MqttGatewayPublish messageSender;

    @Autowired
    private IDeviceMapper mapper;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Autowired
    private IMqttTopicService topicService;

    @Autowired
    private IWorkspaceService workspaceService;

    @Autowired
    private IDevicePayloadService payloadService;

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDeviceFirmwareService deviceFirmwareService;

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Autowired
    private StatusSubscribe statusSubscribe;

    @Autowired
    private StateSubscribe stateSubscribe;

    @Autowired
    private OsdSubscribe osdSubscribe;

    @Autowired
    private ServicesSubscribe servicesSubscribe;

    @Autowired
    private EventsSubscribe eventsSubscribe;

    @Autowired
    private RequestsSubscribe requestsSubscribe;

    @Autowired
    private PropertySetSubscribe propertySetSubscribe;

    @Autowired
    private AbstractPropertyService abstractPropertyService;

    @Autowired
    private AbstractFirmwareService abstractFirmwareService;

    @Override
    public void subDeviceOffline(String deviceSn) {
        // 如果缓存中不存在有关该设备的信息，则认为无人机处于离线状态。
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        if (deviceOpt.isEmpty()) {
            log.debug("无人机已经离线。");
            return;
        }
        try {
            gatewayOnlineSubscribeTopic(SDKManager.getDeviceSDK(String.valueOf(deviceOpt.get().getParentSn())));
        } catch (CloudSDKException e) {
            log.debug("网关已离线。", e);
        }
        deviceRedisService.subDeviceOffline(deviceSn);
        // 在当前工作空间中发布最新的设备拓扑信息。
        pushDeviceOfflineTopo(deviceOpt.get().getWorkspaceId(), deviceSn);
        log.debug("{} 离线的", deviceSn);
    }

    @Override
    public void gatewayOffline(String gatewaySn) {
        // 如果缓存中不存在有关该设备的信息，则认为无人机处于离线状态。
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(gatewaySn);
        if (deviceOpt.isEmpty()) {
            log.debug("网关已离线。");
            return;
        }

        deviceRedisService.subDeviceOffline(deviceOpt.get().getChildDeviceSn());
        deviceRedisService.gatewayOffline(gatewaySn);
        offlineUnsubscribeTopic(SDKManager.getDeviceSDK(gatewaySn));
        // 在当前工作空间中发布最新的设备拓扑信息。
        pushDeviceOfflineTopo(deviceOpt.get().getWorkspaceId(), gatewaySn);
        log.debug("{} 离线的。", gatewaySn);
    }

    @Override
    public void gatewayOnlineSubscribeTopic(GatewayManager gateway) {
        statusSubscribe.subscribe(gateway);
        stateSubscribe.subscribe(gateway, true);
        osdSubscribe.subscribe(gateway, true);
        servicesSubscribe.subscribe(gateway);
        eventsSubscribe.subscribe(gateway, true);
        requestsSubscribe.subscribe(gateway);
        propertySetSubscribe.subscribe(gateway);
    }

    @Override
    public void subDeviceOnlineSubscribeTopic(GatewayManager gateway) {
        statusSubscribe.subscribe(gateway);
        stateSubscribe.subscribe(gateway, false);
        osdSubscribe.subscribe(gateway, false);
        servicesSubscribe.subscribe(gateway);
        eventsSubscribe.subscribe(gateway, false);
        requestsSubscribe.subscribe(gateway);
        propertySetSubscribe.subscribe(gateway);
    }

    @Override
    public void offlineUnsubscribeTopic(GatewayManager gateway) {
        statusSubscribe.unsubscribe(gateway);
        stateSubscribe.unsubscribe(gateway);
        osdSubscribe.unsubscribe(gateway);
        servicesSubscribe.unsubscribe(gateway);
        eventsSubscribe.unsubscribe(gateway);
        requestsSubscribe.unsubscribe(gateway);
        propertySetSubscribe.unsubscribe(gateway);
    }

    @Override
    public List<DeviceDTO> getDevicesByParams(DeviceQueryParam param) {
        return mapper.selectList(
                        new LambdaQueryWrapper<DeviceEntity>()
                                .eq(StringUtils.hasText(param.getDeviceSn()),
                                        DeviceEntity::getDeviceSn, param.getDeviceSn())
                                .eq(param.getDeviceType() != null,
                                        DeviceEntity::getDeviceType, param.getDeviceType())
                                .eq(param.getSubType() != null,
                                        DeviceEntity::getSubType, param.getSubType())
                                .eq(StringUtils.hasText(param.getChildSn()),
                                        DeviceEntity::getChildSn, param.getChildSn())
                                .and(!CollectionUtils.isEmpty(param.getDomains()), wrapper -> {
                                    for (Integer domain : param.getDomains()) {
                                        wrapper.eq(DeviceEntity::getDomain, domain).or();
                                    }
                                })
                                .eq(StringUtils.hasText(param.getWorkspaceId()),
                                        DeviceEntity::getWorkspaceId, param.getWorkspaceId())
                                .eq(param.getBoundStatus() != null, DeviceEntity::getBoundStatus, param.getBoundStatus())
                                .orderBy(param.isOrderBy(),
                                        param.isAsc(), DeviceEntity::getId))
                .stream()
                .map(this::deviceEntityConvertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDTO> getDevicesTopoForWeb(String workspaceId) {
        List<DeviceDTO> devicesList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .workspaceId(workspaceId)
                        .domains(List.of(DeviceDomainEnum.REMOTER_CONTROL.getDomain(), DeviceDomainEnum.DOCK.getDomain()))
                        .build());

        devicesList.stream()
                .filter(gateway -> DeviceDomainEnum.DOCK == gateway.getDomain() ||
                        deviceRedisService.checkDeviceOnline(gateway.getDeviceSn()))
                .forEach(this::spliceDeviceTopo);

        return devicesList;
    }

    @Override
    public void spliceDeviceTopo(DeviceDTO gateway) {

        gateway.setStatus(deviceRedisService.checkDeviceOnline(gateway.getDeviceSn()));

        // 子设备
        if (!StringUtils.hasText(gateway.getChildDeviceSn())) {
            return;
        }

        DeviceDTO subDevice = getDevicesByParams(DeviceQueryParam.builder().deviceSn(gateway.getChildDeviceSn()).build()).get(0);
        subDevice.setStatus(deviceRedisService.checkDeviceOnline(subDevice.getDeviceSn()));
        gateway.setChildren(subDevice);

        // 有效载荷
        subDevice.setPayloadsList(payloadService.getDevicePayloadEntitiesByDeviceSn(gateway.getChildDeviceSn()));
    }

    @Override
    public Optional<TopologyDeviceDTO> getDeviceTopoForPilot(String sn) {
        if (!StringUtils.hasText(sn)) {
            return Optional.empty();
        }
        List<TopologyDeviceDTO> topologyDeviceList = this.getDevicesByParams(
                        DeviceQueryParam.builder()
                                .deviceSn(sn)
                                .build())
                .stream()
                .map(this::deviceConvertToTopologyDTO)
                .collect(Collectors.toList());
        if (topologyDeviceList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(topologyDeviceList.get(0));
    }

    @Override
    public TopologyDeviceDTO deviceConvertToTopologyDTO(DeviceDTO device) {
        if (device == null) {
            return null;
        }
        return new TopologyDeviceDTO()
                .setSn(device.getDeviceSn())
                .setDeviceCallsign(device.getNickname())
                .setDeviceModel(new TopologyDeviceModel()
                        .setDomain(device.getDomain())
                        .setSubType(device.getSubType())
                        .setType(device.getType())
                        .setDeviceModelKey(DeviceEnum.find(device.getDomain(), device.getType(), device.getSubType())))
                .setIconUrls(device.getIconUrl())
                .setOnlineStatus(deviceRedisService.checkDeviceOnline(device.getDeviceSn()))
                .setUserCallsign(device.getNickname())
                .setBoundStatus(device.getBoundStatus())
                .setModel(device.getDeviceName())
                .setUserId(device.getUserId())
                .setDomain(device.getDomain())
                .setGatewaySn(device.getParentSn());
    }

    @Override
    public void pushDeviceOfflineTopo(String workspaceId, String deviceSn) {
        webSocketMessageService.sendBatch(
                workspaceId, null, com.efuav.sdk.websocket.BizCodeEnum.DEVICE_OFFLINE.getCode(),
                new TopologyDeviceDTO().setSn(deviceSn).setOnlineStatus(false));
    }

    @Override
    public void pushDeviceOnlineTopo(String workspaceId, String gatewaySn, String deviceSn) {
        webSocketMessageService.sendBatch(
                workspaceId, null, com.efuav.sdk.websocket.BizCodeEnum.DEVICE_ONLINE.getCode(),
                getDeviceTopoForPilot(deviceSn).orElseGet(TopologyDeviceDTO::new).setGatewaySn(gatewaySn));
    }

    @Override
    public void pushOsdDataToPilot(String workspaceId, String sn, DeviceOsdHost data) {
        webSocketMessageService.sendBatch(
                workspaceId, UserTypeEnum.PILOT.getVal(), com.efuav.sdk.websocket.BizCodeEnum.DEVICE_OSD.getCode(),
                new DeviceOsdWsResponse()
                        .setSn(sn)
                        .setHost(data));
    }

    @Override
    public void pushOsdDataToWeb(String workspaceId, BizCodeEnum codeEnum, String sn, Object data) {
        webSocketMessageService.sendBatch(
                workspaceId, UserTypeEnum.WEB.getVal(), codeEnum.getCode(), TelemetryDTO.builder().sn(sn).host(data).build());
    }

    /**
     * 保存设备信息，如果设备已经存在，则直接更新信息。
     *
     * @param device
     * @return
     */
    @Override
    public Boolean saveOrUpdateDevice(DeviceDTO device) {
        int count = mapper.selectCount(
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDeviceSn, device.getDeviceSn()));
        return count > 0 ? updateDevice(device) : saveDevice(device) > 0;
    }

    /**
     * 保存设备信息。
     *
     * @param device
     * @return
     */
    @Override
    public Integer saveDevice(DeviceDTO device) {
        DeviceEntity entity = deviceDTO2Entity(device);
        return mapper.insert(entity) > 0 ? entity.getId() : -1;
    }

    /**
     * 将数据库实体对象转换为设备数据传输对象。
     *
     * @param entity
     * @return
     */
    private DeviceDTO deviceEntityConvertToDTO(DeviceEntity entity) {
        if (entity == null) {
            return null;
        }
        DeviceDTO.DeviceDTOBuilder builder = DeviceDTO.builder();
        try {
            builder
                    .deviceSn(entity.getDeviceSn())
                    .childDeviceSn(entity.getChildSn())
                    .deviceName(entity.getDeviceName())
                    .deviceDesc(entity.getDeviceDesc())
                    .controlSource(ControlSourceEnum.find(entity.getDeviceIndex()))
                    .workspaceId(entity.getWorkspaceId())
                    .type(DeviceTypeEnum.find(entity.getDeviceType()))
                    .subType(DeviceSubTypeEnum.find(entity.getSubType()))
                    .domain(DeviceDomainEnum.find(entity.getDomain()))
                    .iconUrl(new DeviceIconUrl()
                            .setNormalIconUrl(entity.getUrlNormal())
                            .setSelectIconUrl(entity.getUrlSelect()))
                    .boundStatus(entity.getBoundStatus())
                    .loginTime(entity.getLoginTime() != null ?
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getLoginTime()), ZoneId.systemDefault())
                            : null)
                    .boundTime(entity.getBoundTime() != null ?
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getBoundTime()), ZoneId.systemDefault())
                            : null)
                    .nickname(entity.getNickname())
                    .firmwareVersion(entity.getFirmwareVersion())
                    .workspaceName(entity.getWorkspaceId() != null ?
                            workspaceService.getWorkspaceByWorkspaceId(entity.getWorkspaceId())
                                    .map(WorkspaceDTO::getWorkspaceName).orElse("") : "")
                    .firmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE)
                    .thingVersion(entity.getVersion()).build();
        } catch (CloudSDKException e) {
            log.error(e.getLocalizedMessage() + "实体: {}", entity);
        }
        DeviceDTO deviceDTO = builder.build();
        addFirmwareStatus(deviceDTO, entity);
        return deviceDTO;
    }

    private void addFirmwareStatus(DeviceDTO deviceDTO, DeviceEntity entity) {
        if (!StringUtils.hasText(entity.getFirmwareVersion())) {
            return;
        }
        // 查询设备是否正在更新固件。
        Optional<EventsReceiver<OtaProgress>> progressOpt = deviceRedisService.getFirmwareUpgradingProgress(entity.getDeviceSn());
        if (progressOpt.isPresent()) {
            deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.UPGRADING);
            deviceDTO.setFirmwareProgress(progressOpt.map(EventsReceiver::getOutput)
                    .map(OtaProgress::getProgress)
                    .map(OtaProgressData::getPercent)
                    .orElse(0));
            return;
        }

        //首先查询设备型号的最新固件版本，并将其与当前固件版本进行比较
        //看看是否需要升级。
        Optional<DeviceFirmwareNoteDTO> firmwareReleaseNoteOpt = deviceFirmwareService.getLatestFirmwareReleaseNote(entity.getDeviceName());
        if (firmwareReleaseNoteOpt.isEmpty()) {
            deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.NOT_UPGRADE);
            return;
        }
        if (entity.getFirmwareVersion().equals(firmwareReleaseNoteOpt.get().getProductVersion())) {
            deviceDTO.setFirmwareStatus(entity.getCompatibleStatus() ?
                    DeviceFirmwareStatusEnum.NOT_UPGRADE :
                    DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE);
            return;
        }
        deviceDTO.setFirmwareStatus(DeviceFirmwareStatusEnum.NORMAL_UPGRADE);
    }

    @Override
    public Boolean updateDevice(DeviceDTO deviceDTO) {
        int update = mapper.update(this.deviceDTO2Entity(deviceDTO),
                new LambdaUpdateWrapper<DeviceEntity>().eq(DeviceEntity::getDeviceSn, deviceDTO.getDeviceSn()));
        return update > 0;
    }

    @Override
    public Boolean bindDevice(DeviceDTO device) {
        device.setBoundStatus(true);
        device.setBoundTime(LocalDateTime.now());

        boolean isUpd = this.updateDevice(device);
        if (!isUpd) {
            return false;
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(device.getDeviceSn());
        if (deviceOpt.isEmpty()) {
            return false;
        }

        DeviceDTO redisDevice = deviceOpt.get();
        redisDevice.setWorkspaceId(device.getWorkspaceId());
        deviceRedisService.setDeviceOnline(redisDevice);

        String gatewaySn, deviceSn;
        if (DeviceDomainEnum.REMOTER_CONTROL == redisDevice.getDomain()) {
            gatewaySn = device.getDeviceSn();
            deviceSn = redisDevice.getChildDeviceSn();
        } else {
            gatewaySn = redisDevice.getParentSn();
            deviceSn = device.getDeviceSn();
        }

        pushDeviceOnlineTopo(device.getWorkspaceId(), gatewaySn, deviceSn);
        subDeviceOnlineSubscribeTopic(SDKManager.getDeviceSDK(gatewaySn));
        return true;
    }

    @Override
    public PaginationData<DeviceDTO> getBoundDevicesWithDomain(String workspaceId, Long page,
                                                               Long pageSize, Integer domain) {

        Page<DeviceEntity> pagination = mapper.selectPage(new Page<>(page, pageSize),
                new LambdaQueryWrapper<DeviceEntity>()
                        .eq(DeviceEntity::getDomain, domain)
                        .eq(DeviceEntity::getWorkspaceId, workspaceId)
                        .eq(DeviceEntity::getBoundStatus, true));
        List<DeviceDTO> devicesList = pagination.getRecords().stream().map(this::deviceEntityConvertToDTO)
                .peek(device -> {
                    device.setStatus(deviceRedisService.checkDeviceOnline(device.getDeviceSn()));
                    if (StringUtils.hasText(device.getChildDeviceSn())) {
                        Optional<DeviceDTO> childOpt = this.getDeviceBySn(device.getChildDeviceSn());
                        childOpt.ifPresent(child -> {
                            child.setStatus(deviceRedisService.checkDeviceOnline(child.getDeviceSn()));
                            child.setWorkspaceName(device.getWorkspaceName());
                            device.setChildren(child);
                        });
                    }
                })
                .collect(Collectors.toList());
        return new PaginationData<DeviceDTO>(devicesList, new Pagination(pagination.getCurrent(), pagination.getSize(), pagination.getTotal()));
    }

    @Override
    public void unbindDevice(String deviceSn) {

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        if (deviceOpt.isPresent()) {
            subDeviceOffline(deviceSn);
        } else {
            deviceOpt = getDeviceBySn(deviceSn);
        }
        if (deviceOpt.isEmpty()) {
            return;
        }
        DeviceDTO device = DeviceDTO.builder()
                .deviceSn(deviceSn)
                .workspaceId("")
                .userId("")
                .boundStatus(false)
                .build();
        this.updateDevice(device);
    }

    @Override
    public Optional<DeviceDTO> getDeviceBySn(String sn) {
        List<DeviceDTO> devicesList = this.getDevicesByParams(DeviceQueryParam.builder().deviceSn(sn).build());
        if (devicesList.isEmpty()) {
            return Optional.empty();
        }
        DeviceDTO device = devicesList.get(0);
        device.setStatus(deviceRedisService.checkDeviceOnline(sn));
        return Optional.of(device);
    }

    @Override
    public HttpResultResponse createDeviceOtaJob(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        List<OtaCreateDevice> deviceOtaFirmwares = deviceFirmwareService.getDeviceOtaFirmware(workspaceId, upgradeDTOS);
        if (deviceOtaFirmwares.isEmpty()) {
            return HttpResultResponse.error();
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceOtaFirmwares.get(0).getSn());
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("设备处于离线状态。");
        }
        DeviceDTO device = deviceOpt.get();
        String gatewaySn = DeviceDomainEnum.DOCK == device.getDomain() ? device.getDeviceSn() : device.getParentSn();

        checkOtaConditions(gatewaySn);

        TopicServicesResponse<ServicesReplyData<OtaCreateResponse>> response = abstractFirmwareService.otaCreate(
                SDKManager.getDeviceSDK(gatewaySn), new OtaCreateRequest().setDevices(deviceOtaFirmwares));
        ServicesReplyData<OtaCreateResponse> serviceReply = response.getData();
        String bid = response.getBid();
        if (!serviceReply.getResult().isSuccess()) {
            return HttpResultResponse.error(serviceReply.getResult());
        }

        // 记录需要更新的设备状态。
        deviceOtaFirmwares.forEach(deviceOta -> deviceRedisService.setFirmwareUpgrading(deviceOta.getSn(),
                EventsReceiver.<OtaProgress>builder().bid(bid).sn(deviceOta.getSn()).build()));
        return HttpResultResponse.success();
    }

    /**
     * 确定是否可以升级固件。
     *
     * @param dockSn
     */
    private void checkOtaConditions(String dockSn) {
        Optional<OsdDock> deviceOpt = deviceRedisService.getDeviceOsd(dockSn, OsdDock.class);
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("机场处于离线状态。");
        }
        boolean emergencyStopState = deviceOpt.get().getEmergencyStopState();
        if (emergencyStopState) {
            throw new RuntimeException("机场的紧急停止按钮被按下，无法升级。");
        }

        DockModeCodeEnum dockMode = this.getDockMode(dockSn);
        if (DockModeCodeEnum.IDLE != dockMode) {
            throw new RuntimeException("机场的当前状态无法升级。");
        }
    }

    @Override
    public int devicePropertySet(String workspaceId, String dockSn, JsonNode param) {
        String property = param.fieldNames().next();
        PropertySetFieldEnum propertyEnum = PropertySetFieldEnum.find(property);

        Optional<DeviceDTO> dockOpt = deviceRedisService.getDeviceOnline(dockSn);
        if (dockOpt.isEmpty()) {
            throw new RuntimeException("机场处于离线状态。");
        }
        String childSn = dockOpt.get().getChildDeviceSn();
        Optional<OsdDockDrone> osdOpt = deviceRedisService.getDeviceOsd(childSn, OsdDockDrone.class);
        if (osdOpt.isEmpty()) {
            throw new RuntimeException("设备处于离线状态。");
        }

        // 请确保数据有效。
        BasicDeviceProperty basicDeviceProperty = objectMapper.convertValue(param.get(property), propertyEnum.getClazz());
        boolean valid = basicDeviceProperty.valid();
        if (!valid) {
            throw new IllegalArgumentException(CommonErrorEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        boolean isPublish = basicDeviceProperty.canPublish(osdOpt.get());
        if (!isPublish) {
            return PropertySetReplyResultEnum.SUCCESS.getResult();
        }
        BaseModel baseModel = objectMapper.convertValue(param, propertyEnum.getProperty().getClazz());
        PropertySetReplyResultEnum result = abstractPropertyService.propertySet(
                SDKManager.getDeviceSDK(dockSn), propertyEnum.getProperty(), baseModel);
        return result.getResult();
    }

    @Override
    public DockModeCodeEnum getDockMode(String dockSn) {
        return deviceRedisService.getDeviceOsd(dockSn, OsdDock.class)
                .map(OsdDock::getModeCode).orElse(null);
    }

    @Override
    public DroneModeCodeEnum getDeviceMode(String deviceSn) {
        return deviceRedisService.getDeviceOsd(deviceSn, OsdDockDrone.class)
                .map(OsdDockDrone::getModeCode).orElse(DroneModeCodeEnum.DISCONNECTED);
    }

    @Override
    public Boolean checkDockDrcMode(String dockSn) {
        return deviceRedisService.getDeviceOsd(dockSn, OsdDock.class)
                .map(OsdDock::getDrcState)
                .orElse(DrcStateEnum.DISCONNECTED) != DrcStateEnum.DISCONNECTED;
    }

    @Override
    public Boolean checkAuthorityFlight(String gatewaySn) {
        return deviceRedisService.getDeviceOnline(gatewaySn).flatMap(gateway ->
                        Optional.of((DeviceDomainEnum.DOCK == gateway.getDomain()
                                || DeviceDomainEnum.REMOTER_CONTROL == gateway.getDomain())
                                && ControlSourceEnum.A == gateway.getControlSource()))
                .orElse(true);
    }

    @Override
    public void updateFlightControl(DeviceDTO gateway, ControlSourceEnum controlSource) {
        if (controlSource == gateway.getControlSource()) {
            return;
        }
        gateway.setControlSource(controlSource);
        deviceRedisService.setDeviceOnline(gateway);

        webSocketMessageService.sendBatch(gateway.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                BizCodeEnum.CONTROL_SOURCE_CHANGE.getCode(),
                DeviceAuthorityDTO.builder()
                        .controlSource(gateway.getControlSource())
                        .sn(gateway.getDeviceSn())
                        .type(DroneAuthorityEnum.FLIGHT)
                        .build());
    }

    /**
     * 获取所有绑定的设备信息
     *
     * @return
     */
    @Override
    public List<DeviceDTO> getDevices() {
        List<DeviceDTO> devicesList = this.getDevicesByParams(
                DeviceQueryParam.builder()
                        .domains(List.of(DeviceDomainEnum.REMOTER_CONTROL.getDomain(), DeviceDomainEnum.DOCK.getDomain()))
                        .build());

        devicesList.stream()
                .filter(gateway -> DeviceDomainEnum.DOCK == gateway.getDomain() ||
                        deviceRedisService.checkDeviceOnline(gateway.getDeviceSn()))
                .forEach(this::spliceDeviceTopo);
        return devicesList;
    }

    /**
     * 将设备数据传输对象转换为数据库实体对象。
     *
     * @param dto
     * @return
     */
    private DeviceEntity deviceDTO2Entity(DeviceDTO dto) {
        DeviceEntity.DeviceEntityBuilder builder = DeviceEntity.builder();
        if (dto == null) {
            return builder.build();
        }

        return builder.deviceSn(dto.getDeviceSn())
                .deviceIndex(Optional.ofNullable(dto.getControlSource())
                        .map(ControlSourceEnum::getControlSource).orElse(null))
                .deviceName(dto.getDeviceName())
                .version(dto.getThingVersion())
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .workspaceId(dto.getWorkspaceId())
                .boundStatus(dto.getBoundStatus())
                .domain(Optional.ofNullable(dto.getDomain()).map(DeviceDomainEnum::getDomain).orElse(null))
                .deviceType(Optional.ofNullable(dto.getType()).map(DeviceTypeEnum::getType).orElse(null))
                .subType(Optional.ofNullable(dto.getSubType()).map(DeviceSubTypeEnum::getSubType).orElse(null))
                .loginTime(dto.getLoginTime() != null ?
                        dto.getLoginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .boundTime(dto.getBoundTime() != null ?
                        dto.getBoundTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .childSn(dto.getChildDeviceSn())
                .firmwareVersion(dto.getFirmwareVersion())
                .compatibleStatus(dto.getFirmwareStatus() == null ? null :
                        DeviceFirmwareStatusEnum.CONSISTENT_UPGRADE != dto.getFirmwareStatus())
                .deviceDesc(dto.getDeviceDesc())
                .build();
    }
}