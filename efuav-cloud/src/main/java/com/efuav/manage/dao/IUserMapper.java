package com.efuav.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.efuav.manage.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface IUserMapper extends BaseMapper<UserEntity> {

}
