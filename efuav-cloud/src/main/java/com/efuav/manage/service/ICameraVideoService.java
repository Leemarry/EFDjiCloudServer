package com.efuav.manage.service;

import com.efuav.manage.model.dto.CapacityVideoDTO;
import com.efuav.manage.model.receiver.CapacityVideoReceiver;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
public interface ICameraVideoService {

    /**
     * 将接收到的镜头能力对象转换为镜头数据传输对象。
     * @param receiver
     * @return  data 传输对象
     */
    CapacityVideoDTO receiver2Dto(CapacityVideoReceiver receiver);
}
