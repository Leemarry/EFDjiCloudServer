package com.efuav.sdk.websocket.api;

import com.efuav.sdk.common.Common;
import com.efuav.sdk.exception.CloudSDKErrorEnum;
import com.efuav.sdk.exception.CloudSDKException;
import com.efuav.sdk.websocket.ConcurrentWebSocketSession;
import com.efuav.sdk.websocket.WebSocketMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Collection;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/24
 */
public class WebSocketMessageSend {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageSend.class);

    public void sendMessage(ConcurrentWebSocketSession session, WebSocketMessageResponse message) {
        if (session == null) {
            return;
        }

        try {
            if (!session.isOpen()) {
                session.close();
                log.info("此会话已关闭。");
                return;
            }

            session.sendMessage(new TextMessage(Common.getObjectMapper().writeValueAsBytes(message)));
        } catch (IOException e) {
            throw new CloudSDKException(CloudSDKErrorEnum.WEBSOCKET_PUBLISH_ABNORMAL, e.getLocalizedMessage());
        }
    }

    public void sendBatch(Collection<ConcurrentWebSocketSession> sessions, WebSocketMessageResponse message) {
        if (sessions.isEmpty()) {
            return;
        }

        try {

            TextMessage data = new TextMessage(Common.getObjectMapper().writeValueAsBytes(message));

            for (ConcurrentWebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    session.close();
                    log.info("此会话已关闭。");
                    return;
                }
                session.sendMessage(data);
            }

        } catch (IOException e) {
            throw new CloudSDKException(CloudSDKErrorEnum.WEBSOCKET_PUBLISH_ABNORMAL, e.getLocalizedMessage());
        }
    }
}