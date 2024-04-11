package com.efuav.component.websocket.service;

import com.efuav.component.websocket.config.MyConcurrentWebSocketSession;

import java.util.Collection;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/25
 */
public interface IWebSocketManageService {

    void put(String key, MyConcurrentWebSocketSession val);

    void remove(String key, String sessionId);

    Collection<MyConcurrentWebSocketSession> getValueWithWorkspace(String workspaceId);

    Collection<MyConcurrentWebSocketSession> getValueWithWorkspaceAndUserType(String workspaceId, Integer userType);

    Long getConnectedCount();
}
