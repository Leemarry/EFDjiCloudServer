package com.efuav.control.service;

import com.efuav.control.model.dto.JwtAclDTO;
import com.efuav.control.model.param.DrcConnectParam;
import com.efuav.control.model.param.DrcModeParam;
import com.efuav.sdk.cloudapi.control.DrcModeMqttBroker;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
public interface IDrcService {

    /**
     * 在redis中保存dock的drc模式。
     * @param dockSn
     * @param clientId
     */
    void setDrcModeInRedis(String dockSn, String clientId);

    /**
     * 查询正在控制dock的客户端。
     * @param dockSn
     * @return clientId
     */
    String getDrcModeInRedis(String dockSn);

    /**
     * 删除redis中dock的drc模式。
     * @param dockSn
     * @return
     */
    Boolean delDrcModeInRedis(String dockSn);

    /**
     * 为控制终端提供mqtt选项。
     * @param workspaceId
     * @param userId
     * @param username
     * @param param
     * @return
     */
    DrcModeMqttBroker userDrcAuth(String workspaceId, String userId, String username, DrcConnectParam param);

    /**
     * 使dock进入drc模式。并授予相关权限。
     * @param workspaceId
     * @param param
     * @return
     */
    JwtAclDTO deviceDrcEnter(String workspaceId, DrcModeParam param);

    /**
     * 使dock退出drc模式。
     * @param workspaceId
     * @param param
     */
    void deviceDrcExit(String workspaceId, DrcModeParam param);
}
