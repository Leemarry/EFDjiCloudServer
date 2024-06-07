package com.efuav.media.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.efuav.media.model.MediaFileEntity;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Service
public interface IFileMapper extends BaseMapper<MediaFileEntity> {
}
