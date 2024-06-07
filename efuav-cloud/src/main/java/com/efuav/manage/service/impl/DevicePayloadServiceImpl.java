package com.efuav.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.efuav.component.websocket.model.BizCodeEnum;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.control.model.enums.DroneAuthorityEnum;
import com.efuav.manage.dao.IDevicePayloadMapper;
import com.efuav.manage.model.dto.*;
import com.efuav.manage.model.entity.DevicePayloadEntity;
import com.efuav.manage.model.enums.UserTypeEnum;
import com.efuav.manage.service.ICapacityCameraService;
import com.efuav.manage.service.IDeviceDictionaryService;
import com.efuav.manage.service.IDevicePayloadService;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.sdk.cloudapi.device.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Slf4j
@Service
@Transactional
public class DevicePayloadServiceImpl implements IDevicePayloadService {

    @Autowired
    private IDevicePayloadMapper mapper;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private IWebSocketMessageService sendMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Override
    public Integer checkPayloadExist(String payloadSn) {
        DevicePayloadEntity devicePayload = mapper.selectOne(
                new LambdaQueryWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getPayloadSn, payloadSn));
        return devicePayload != null ? devicePayload.getId() : -1;
    }

    private Integer saveOnePayloadEntity(DevicePayloadEntity entity) {
        int id = this.checkPayloadExist(entity.getPayloadSn());
        // 如果它已经存在，请直接更新数据。
        if (id > 0) {
            entity.setId(id);
            // 对于无人机本身的有效载荷，没有固件版本。
            entity.setFirmwareVersion(null);
            return mapper.updateById(entity) > 0 ? entity.getId() : 0;
        }
        return mapper.insert(entity) > 0 ? entity.getId() : 0;
    }

    @Override
    public Boolean savePayloadDTOs(DeviceDTO device, List<DevicePayloadReceiver> payloadReceiverList) {
        Map<String, ControlSourceEnum> controlMap = CollectionUtils.isEmpty(device.getPayloadsList()) ?
                Collections.emptyMap() : device.getPayloadsList().stream()
                    .collect(Collectors.toMap(DevicePayloadDTO::getPayloadSn, DevicePayloadDTO::getControlSource));

        for (DevicePayloadReceiver payloadReceiver : payloadReceiverList) {
            payloadReceiver.setDeviceSn(device.getDeviceSn());
            int payloadId = this.saveOnePayloadDTO(payloadReceiver);
            if (payloadId <= 0) {
                log.error("有效负载数据保存失败。");
                return false;
            }
            if (controlMap.get(payloadReceiver.getSn()) != payloadReceiver.getControlSource()) {
                sendMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                                    BizCodeEnum.CONTROL_SOURCE_CHANGE.getCode(),
                                    DeviceAuthorityDTO.builder()
                                            .controlSource(payloadReceiver.getControlSource())
                                            .sn(payloadReceiver.getSn())
                                            .type(DroneAuthorityEnum.PAYLOAD)
                                            .build());
            }
        }

        List<DevicePayloadDTO> payloads = this.getDevicePayloadEntitiesByDeviceSn(device.getDeviceSn());
        device.setPayloadsList(payloads);
        deviceRedisService.setDeviceOnline(device);
        return true;
    }

    @Override
    public Integer saveOnePayloadDTO(DevicePayloadReceiver payloadReceiver) {
        return this.saveOnePayloadEntity(receiverConvertToEntity(payloadReceiver));
    }

    @Override
    public List<DevicePayloadDTO> getDevicePayloadEntitiesByDeviceSn(String deviceSn) {
        return mapper.selectList(
                new LambdaQueryWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getDeviceSn, deviceSn))
                .stream()
                .map(this::payloadEntityConvertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePayloadsByDeviceSn(List<String> deviceSns) {
        deviceSns.forEach(deviceSn -> {
            mapper.delete(
                    new LambdaQueryWrapper<DevicePayloadEntity>()
                            .eq(DevicePayloadEntity::getDeviceSn, deviceSn));
            capacityCameraService.deleteCapacityCameraByDeviceSn(deviceSn);
        });
    }

    @Override
    public Boolean updateFirmwareVersion(String droneSn, PayloadFirmwareVersion receiver) {
        return mapper.update(DevicePayloadEntity.builder()
                        .firmwareVersion(receiver.getFirmwareVersion()).build(),
                new LambdaUpdateWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getDeviceSn, droneSn)
                        .eq(DevicePayloadEntity::getPayloadSn, droneSn + "-" + receiver.getPosition().getPosition())
        ) > 0;
    }

    /**
     * 处理设备的有效负载数据。
     * @param drone
     * @param payloads
     */
    @Override
    public void updatePayloadControl(DeviceDTO drone, List<DevicePayloadReceiver> payloads) {
        boolean match = payloads.stream().peek(p -> p.setSn(Objects.requireNonNullElse(p.getSn(),
                p.getDeviceSn() + "-" + p.getPayloadIndex().getPosition().getPosition())))
                .anyMatch(p -> ControlSourceEnum.UNKNOWN == p.getControlSource());
        if (match) {
            return;
        }

        if (payloads.isEmpty()) {
            drone.setPayloadsList(null);
            this.deletePayloadsByDeviceSn(List.of(drone.getDeviceSn()));
            deviceRedisService.setDeviceOnline(drone);
            return;
        }

        // 筛选未保存的有效负载信息。
        Set<String> payloadSns = this.getDevicePayloadEntitiesByDeviceSn(drone.getDeviceSn())
                .stream().map(DevicePayloadDTO::getPayloadSn).collect(Collectors.toSet());

        Set<String> newPayloadSns = payloads.stream().map(DevicePayloadReceiver::getSn).collect(Collectors.toSet());
        payloadSns.removeAll(newPayloadSns);
        this.deletePayloadsByPayloadsSn(payloadSns);

        // 保存新的有效载荷信息。
        boolean isSave = this.savePayloadDTOs(drone, payloads);
        log.debug("保存有效载荷的结果是 {}.", isSave);
    }

    @Override
    public void deletePayloadsByPayloadsSn(Collection<String> payloadSns) {
        if (CollectionUtils.isEmpty(payloadSns)) {
            return;
        }
        mapper.delete(new LambdaUpdateWrapper<DevicePayloadEntity>()
                .or(wrapper -> payloadSns.forEach(sn -> wrapper.eq(DevicePayloadEntity::getPayloadSn, sn))));
    }

    @Override
    public Boolean checkAuthorityPayload(String deviceSn, String payloadIndex) {
        return deviceRedisService.getDeviceOnline(deviceSn).flatMap(device ->
                Optional.of(DeviceDomainEnum.DRONE == device.getDomain()
                        && !CollectionUtils.isEmpty(device.getPayloadsList())
                        && ControlSourceEnum.A ==
                        device.getPayloadsList().stream()
                                .filter(payload -> payloadIndex.equals(payload.getPayloadIndex().toString()))
                                .map(DevicePayloadDTO::getControlSource).findAny()
                                .orElse(ControlSourceEnum.B)))
                .orElse(true);
    }

    /**
     * 将数据库实体对象转换为有效负载数据传输对象。
     * @param entity
     * @return
     */
    private DevicePayloadDTO payloadEntityConvertToDTO(DevicePayloadEntity entity) {
        DevicePayloadDTO.DevicePayloadDTOBuilder builder = DevicePayloadDTO.builder();
        if (entity != null) {
            builder.payloadSn(entity.getPayloadSn())
                    .payloadName(entity.getPayloadName())
                    .payloadDesc(entity.getPayloadDesc())
                    .index(entity.getPayloadIndex())
                    .payloadIndex(new PayloadIndex()
                            .setType(DeviceTypeEnum.find(entity.getPayloadType()))
                            .setSubType(DeviceSubTypeEnum.find(entity.getSubType()))
                            .setPosition(PayloadPositionEnum.find(entity.getPayloadIndex())))
                    .controlSource(ControlSourceEnum.find(entity.getControlSource()));
        }
        return builder.build();
    }

    /**
     * 将接收到的有效负载对象转换为数据库实体对象。
     * @param dto   payload
     * @return
     */
    private DevicePayloadEntity receiverConvertToEntity(DevicePayloadReceiver dto) {
        if (dto == null) {
            return new DevicePayloadEntity();
        }
        DevicePayloadEntity.DevicePayloadEntityBuilder builder = DevicePayloadEntity.builder();

        //  cameraIndex由类型和子类型以及悬挂在无人机上的有效载荷的索引组成。
        // type-subType-index
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService.getOneDictionaryInfoByTypeSubType(
                DeviceDomainEnum.PAYLOAD.getDomain(), dto.getPayloadIndex().getType().getType(),
                dto.getPayloadIndex().getSubType().getSubType());
        dictionaryOpt.ifPresent(dictionary ->
                builder.payloadName(dictionary.getDeviceName())
                        .payloadDesc(dictionary.getDeviceDesc()));

        builder.payloadType(dto.getPayloadIndex().getType().getType())
                .subType(dto.getPayloadIndex().getSubType().getSubType())
                .payloadIndex(dto.getPayloadIndex().getPosition().getPosition())
                .controlSource(dto.getControlSource().getControlSource());

        return builder
                .payloadSn(dto.getSn())
                .deviceSn(dto.getDeviceSn())
                .build();
    }

    private DevicePayloadDTO receiver2Dto(DevicePayloadReceiver receiver) {
        DevicePayloadDTO.DevicePayloadDTOBuilder builder = DevicePayloadDTO.builder();
        if (receiver == null) {
            return builder.build();
        }
        return builder.payloadSn(receiver.getSn())
                .payloadIndex(receiver.getPayloadIndex())
                .controlSource(receiver.getControlSource())
                .build();
    }

}