package com.efuav.component.websocket.config;

import com.efuav.component.websocket.service.IWebSocketManageService;
import com.efuav.sdk.websocket.WebSocketDefaultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

/**
 *
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Slf4j
public class MyWebSocketHandler extends WebSocketDefaultHandler {

    private IWebSocketManageService webSocketManageService;

    MyWebSocketHandler(WebSocketHandler delegate, IWebSocketManageService webSocketManageService) {
        super(delegate);
        this.webSocketManageService = webSocketManageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();
        if (StringUtils.hasText(principal.getName())) {
            webSocketManageService.put(principal.getName(), new MyConcurrentWebSocketSession(session));
            log.debug("{} 已连接。 ID: {}. WebSocketSession[当前计数: {}]",
                    principal.getName(), session.getId(), webSocketManageService.getConnectedCount());
            return;
        }
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Principal principal = session.getPrincipal();
        if (StringUtils.hasText(principal.getName())) {
            webSocketManageService.remove(principal.getName(), session.getId());
            log.debug("{} 已断开连接。 ID: {}. WebSocketSession[当前计数: {}]",
                    principal.getName(), session.getId(), webSocketManageService.getConnectedCount());
        }

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.debug("接收信息: {}", message.getPayload());
    }

}