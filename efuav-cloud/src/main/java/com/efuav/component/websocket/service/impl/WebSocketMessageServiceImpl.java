package com.efuav.component.websocket.service.impl;

import com.efuav.component.websocket.config.MyConcurrentWebSocketSession;
import com.efuav.component.websocket.service.IWebSocketManageService;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.sdk.websocket.WebSocketMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
@Service
@Slf4j
public class WebSocketMessageServiceImpl implements IWebSocketMessageService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IWebSocketManageService webSocketManageService;

    @Override
    public void sendMessage(MyConcurrentWebSocketSession session, WebSocketMessageResponse message) {
        if (session == null) {
            return;
        }

        try {
            if (!session.isOpen()) {
                session.close();
                log.debug("此会话已关闭。");
                return;
            }


            session.sendMessage(new TextMessage(mapper.writeValueAsBytes(message)));
        } catch (IOException e) {
            log.info("未能发布消息。 {}", message.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void sendBatch(Collection<MyConcurrentWebSocketSession> sessions, WebSocketMessageResponse message) {
        if (sessions.isEmpty()) {
            return;
        }

        try {

            TextMessage data = new TextMessage(mapper.writeValueAsBytes(message));

            for (MyConcurrentWebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    session.close();
                    log.debug("此会话已关闭。");
                    return;
                }
                session.sendMessage(data);
            }

        } catch (IOException e) {
            log.info("未能发布消息。 {}", message.toString());

            e.printStackTrace();
        }
    }

    @Override
    public void sendBatch(String workspaceId, Integer userType, String bizCode, Object data) {
        if (!StringUtils.hasText(workspaceId)) {
            throw new RuntimeException("工作区ID不存在。");
        }
        Collection<MyConcurrentWebSocketSession> sessions = Objects.isNull(userType) ?
                webSocketManageService.getValueWithWorkspace(workspaceId) :
                webSocketManageService.getValueWithWorkspaceAndUserType(workspaceId, userType);

        this.sendBatch(sessions, new WebSocketMessageResponse()
                        .setData(Objects.requireNonNullElse(data, ""))
                        .setTimestamp(System.currentTimeMillis())
                        .setBizCode(bizCode));
    }

    @Override
    public void sendBatch(String workspaceId, String bizCode, Object data) {
        this.sendBatch(workspaceId, null, bizCode, data);
    }
}