package com.efuav.control.service;

import com.efuav.control.model.enums.DroneAuthorityEnum;
import com.efuav.control.model.enums.RemoteDebugMethodEnum;
import com.efuav.control.model.param.*;
import com.efuav.sdk.common.HttpResultResponse;

/**
 * @author sean
 * @version 1.2
 * @date 2022/7/29
 */
public interface IControlService {

    /**
     * 通过命令远程调试dock。
     * @param sn
     * @param serviceIdentifier
     * @param param
     * @return
     */
    HttpResultResponse controlDockDebug(String sn, RemoteDebugMethodEnum serviceIdentifier, RemoteDebugParam param);

    /**
     * 让无人机飞向目标点。
     * @param sn
     * @param param
     * @return
     */
    HttpResultResponse flyToPoint(String sn, FlyToPointParam param);

    /**
     * 结束无人机飞向目标点的任务。
     * @param sn
     * @return
     */
    HttpResultResponse flyToPointStop(String sn);

    /**
     * 处理飞往目标点的进度结果通知。
     * @param receiver
     * @param headers
     * @return
     */
//    CommonTopicReceiver handleFlyToPointProgress(CommonTopicReceiver receiver, MessageHeaders headers);

    /**
     * 控制无人机起飞。
     * @param sn
     * @param param
     * @return
     */
    HttpResultResponse takeoffToPoint(String sn, TakeoffToPointParam param);

    /**
     * 夺取无人机的控制权或有效载荷控制权。
     * @param sn
     * @param authority
     * @param param
     * @return
     */
    HttpResultResponse seizeAuthority(String sn, DroneAuthorityEnum authority, DronePayloadParam param);

    /**
     * 控制无人机的有效载荷。
     * @param param
     * @return
     */
    HttpResultResponse payloadCommands(PayloadCommandsParam param) throws Exception;
}
