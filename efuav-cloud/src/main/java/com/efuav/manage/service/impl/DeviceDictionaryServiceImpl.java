package com.efuav.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.efuav.manage.dao.IDeviceDictionaryMapper;
import com.efuav.manage.model.dto.DeviceDictionaryDTO;
import com.efuav.manage.model.entity.DeviceDictionaryEntity;
import com.efuav.manage.service.IDeviceDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
@Service
@Transactional
public class DeviceDictionaryServiceImpl implements IDeviceDictionaryService {

    @Autowired
    private IDeviceDictionaryMapper mapper;

    @Override
    public Optional<DeviceDictionaryDTO> getOneDictionaryInfoByTypeSubType(Integer domain, Integer deviceType, Integer subType) {
        if (domain == null || deviceType == null || subType == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                entityConvertToDTO(
                        mapper.selectOne(
                                new LambdaQueryWrapper<DeviceDictionaryEntity>()
                                        .eq(DeviceDictionaryEntity::getDomain, domain)
                                        .eq(DeviceDictionaryEntity::getDeviceType, deviceType)
                                        .eq(DeviceDictionaryEntity::getSubType, subType)
                                        .last(" limit 1 "))));
    }

    /**
     * 将数据库实体对象转换为字典数据传输对象。
     * @param entity
     * @return
     */
    private DeviceDictionaryDTO entityConvertToDTO(DeviceDictionaryEntity entity) {
        DeviceDictionaryDTO.DeviceDictionaryDTOBuilder builder = DeviceDictionaryDTO.builder();

        if (entity != null) {
            builder.deviceName(entity.getDeviceName())
                    .deviceDesc(entity.getDeviceDesc())
                    .deviceType(entity.getDeviceType())
                    .domain(entity.getDomain())
                    .subType(entity.getSubType());
        }
        return builder.build();
    }
}