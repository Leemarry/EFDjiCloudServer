package com.efuav.manage.service.impl;

import com.efuav.component.redis.RedisConst;
import com.efuav.component.redis.RedisOpsUtils;
import com.efuav.manage.model.dto.CapacityCameraDTO;
import com.efuav.manage.model.dto.DeviceDictionaryDTO;
import com.efuav.manage.model.receiver.CapacityCameraReceiver;
import com.efuav.manage.service.ICameraVideoService;
import com.efuav.manage.service.ICapacityCameraService;
import com.efuav.manage.service.IDeviceDictionaryService;
import com.efuav.sdk.cloudapi.device.DeviceDomainEnum;
import com.efuav.sdk.cloudapi.device.PayloadIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Service
public class CapacityCameraServiceImpl implements ICapacityCameraService {

    @Autowired
    private ICameraVideoService cameraVideoService;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Override
    public List<CapacityCameraDTO> getCapacityCameraByDeviceSn(String deviceSn) {
        return (List<CapacityCameraDTO>) RedisOpsUtils.hashGet(RedisConst.LIVE_CAPACITY, deviceSn);
    }

    @Override
    public Boolean deleteCapacityCameraByDeviceSn(String deviceSn) {
        return RedisOpsUtils.hashDel(RedisConst.LIVE_CAPACITY, new String[]{deviceSn});
    }

    @Override
    public void saveCapacityCameraReceiverList(List<CapacityCameraReceiver> capacityCameraReceivers, String deviceSn) {
        List<CapacityCameraDTO> capacity = capacityCameraReceivers.stream()
                .map(this::receiver2Dto).collect(Collectors.toList());
        RedisOpsUtils.hashSet(RedisConst.LIVE_CAPACITY, deviceSn, capacity);
    }

    public CapacityCameraDTO receiver2Dto(CapacityCameraReceiver receiver) {
        CapacityCameraDTO.CapacityCameraDTOBuilder builder = CapacityCameraDTO.builder();
        if (receiver == null) {
            return builder.build();
        }
        PayloadIndex cameraIndex = receiver.getCameraIndex();
        //cameraIndex由类型和子类型以及悬挂在无人机上的有效载荷的索引组成。
        //type subType索引
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService.getOneDictionaryInfoByTypeSubType(
                DeviceDomainEnum.PAYLOAD.getDomain(), cameraIndex.getType().getType(), cameraIndex.getSubType().getSubType());
        dictionaryOpt.ifPresent(dictionary -> builder.name(dictionary.getDeviceName()));

        return builder
                .id(UUID.randomUUID().toString())
                .videosList(receiver.getVideoList()
                        .stream()
                        .map(cameraVideoService::receiver2Dto)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .index(receiver.getCameraIndex().toString())
                .build();
    }
}