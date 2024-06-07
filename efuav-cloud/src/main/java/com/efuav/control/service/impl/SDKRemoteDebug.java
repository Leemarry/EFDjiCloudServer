package com.efuav.control.service.impl;

import com.efuav.component.mqtt.model.EventsReceiver;
import com.efuav.component.websocket.service.IWebSocketMessageService;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.model.enums.UserTypeEnum;
import com.efuav.manage.service.IDeviceRedisService;
import com.efuav.sdk.cloudapi.debug.RemoteDebugProgress;
import com.efuav.sdk.cloudapi.debug.api.AbstractDebugService;
import com.efuav.sdk.mqtt.MqttReply;
import com.efuav.sdk.mqtt.events.EventsDataRequest;
import com.efuav.sdk.mqtt.events.TopicEventsRequest;
import com.efuav.sdk.mqtt.events.TopicEventsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author sean
 * @version 1.7
 * @date 2023/7/4
 */
@Service
@Slf4j
public class SDKRemoteDebug extends AbstractDebugService {

    @Autowired
    private IWebSocketMessageService webSocketMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Override
    public TopicEventsResponse<MqttReply> remoteDebugProgress(TopicEventsRequest<EventsDataRequest<RemoteDebugProgress>> request, MessageHeaders headers) {
        String sn = request.getGateway();

        EventsReceiver<RemoteDebugProgress> eventsReceiver = new EventsReceiver<RemoteDebugProgress>()
                .setOutput(request.getData().getOutput()).setResult(request.getData().getResult());
        eventsReceiver.setBid(request.getBid());
        eventsReceiver.setSn(sn);

        log.info("SN: {}, {} ===> 控制进度: {}", sn, request.getMethod(), eventsReceiver.getOutput().getProgress());

        if (!eventsReceiver.getResult().isSuccess()) {
            log.error("SN: {}, {} ===> Error: {}", sn, request.getMethod(), eventsReceiver.getResult());
        }

        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(sn);

        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("设备处于离线状态。");
        }

        DeviceDTO device = deviceOpt.get();
        webSocketMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                request.getMethod(), eventsReceiver);

        return new TopicEventsResponse<MqttReply>().setData(MqttReply.success());
    }
}
