package com.efuav.component.websocket.service;

import com.efuav.component.websocket.config.MyConcurrentWebSocketSession;
import com.efuav.sdk.websocket.WebSocketMessageResponse;

import java.util.Collection;

/**
 * @author sean.zhou
 * @date 2021/11/24
 * @version 0.1
 */
public interface IWebSocketMessageService {

    /**
     * 向特定连接发送消息。
     * @param session   WebSocket连接对象的集合。
     * @param message   message
     */
    void sendMessage(MyConcurrentWebSocketSession session, WebSocketMessageResponse message);

    /**
     * 向特定连接发送相同的消息。
     * @param sessions  WebSocket连接对象的集合。
     * @param message   message
     */
    void sendBatch(Collection<MyConcurrentWebSocketSession> sessions, WebSocketMessageResponse message);

    void sendBatch(String workspaceId, Integer userType, String bizCode, Object data);

    void sendBatch(String workspaceId, String bizCode, Object data);
}
