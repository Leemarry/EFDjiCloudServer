package com.efuav.manage.service.impl;

import com.efuav.manage.model.receiver.CapacityDeviceReceiver;
import com.efuav.manage.service.ICapacityCameraService;
import com.efuav.sdk.cloudapi.livestream.DockLivestreamAbilityUpdate;
import com.efuav.sdk.cloudapi.livestream.RcLivestreamAbilityUpdate;
import com.efuav.sdk.cloudapi.livestream.api.AbstractLivestreamService;
import com.efuav.sdk.mqtt.state.TopicStateRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/7/6
 */
@Service
public class SDKLivestreamService extends AbstractLivestreamService {

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void dockLivestreamAbilityUpdate(TopicStateRequest<DockLivestreamAbilityUpdate> request, MessageHeaders headers) {
        saveLiveCapacity(request.getData().getLiveCapacity().getDeviceList());
    }

    @Override
    public void rcLivestreamAbilityUpdate(TopicStateRequest<RcLivestreamAbilityUpdate> request, MessageHeaders headers) {
        saveLiveCapacity(request.getData().getLiveCapacity().getDeviceList());
    }

    private void saveLiveCapacity(Object data) {
        List<CapacityDeviceReceiver> devices = objectMapper.convertValue(
                data, new TypeReference<List<CapacityDeviceReceiver>>() {});
        for (CapacityDeviceReceiver capacityDeviceReceiver : devices) {
            capacityCameraService.saveCapacityCameraReceiverList(
                    capacityDeviceReceiver.getCameraList(), capacityDeviceReceiver.getSn());
        }
    }
}
