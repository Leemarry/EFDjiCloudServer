package com.efuav.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.efuav.map.dao.IElementCoordinateMapper;
import com.efuav.map.model.entity.ElementCoordinateEntity;
import com.efuav.map.service.IElementCoordinateService;
import com.efuav.sdk.cloudapi.map.ElementCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Service
@Transactional
public class ElementCoordinateServiceImpl implements IElementCoordinateService {

    @Autowired
    private IElementCoordinateMapper mapper;

    @Override
    public List<ElementCoordinate> getCoordinateByElementId(String elementId) {
        return mapper.selectList(
                new LambdaQueryWrapper<ElementCoordinateEntity>()
                        .eq(ElementCoordinateEntity::getElementId, elementId))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean saveCoordinate(List<ElementCoordinate> coordinateList, String elementId) {
        for (ElementCoordinate coordinate : coordinateList) {
            ElementCoordinateEntity entity = this.dtoConvertToEntity(coordinate);
            entity.setElementId(elementId);

            int insert = mapper.insert(entity);
            if (insert <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean deleteCoordinateByElementId(String elementId) {
        return mapper.delete(new LambdaUpdateWrapper<ElementCoordinateEntity>()
                .eq(ElementCoordinateEntity::getElementId, elementId)) > 0;
    }

    /**
     * 将数据库实体对象转换为坐标数据传输对象。
     * @param entity
     * @return
     */
    private ElementCoordinate entityConvertToDto(ElementCoordinateEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ElementCoordinate()
                .setLongitude(entity.getLongitude())
                .setLatitude(entity.getLatitude())
                .setAltitude(entity.getAltitude());
    }

    /**
     * 将接收到的坐标对象转换为数据库实体对象。
     * @param coordinate
     * @return
     */
    private ElementCoordinateEntity dtoConvertToEntity(ElementCoordinate coordinate) {
        ElementCoordinateEntity.ElementCoordinateEntityBuilder builder = ElementCoordinateEntity.builder();
        if (coordinate == null) {
            return builder.build();
        }

        return builder
                .altitude(coordinate.getAltitude())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();
    }
}
