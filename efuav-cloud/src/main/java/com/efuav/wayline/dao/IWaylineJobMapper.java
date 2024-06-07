package com.efuav.wayline.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.efuav.wayline.model.entity.WaylineJobEntity;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
@Service
public interface IWaylineJobMapper extends BaseMapper<WaylineJobEntity> {
}
