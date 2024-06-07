package com.efuav.component.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 自动填充设定值
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入数据库时自动填充。
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Long.class,
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        this.strictInsertFill(metaObject, "updateTime", Long.class,
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    /**
     * 更新数据时自动填充。
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Long.class,
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
