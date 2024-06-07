package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.DeviceDTO;
import com.efuav.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.efuav.manage.service.IDeviceService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import com.efuav.sdk.exception.CloudSDKErrorEnum;
import com.efuav.sdk.mqtt.property.PropertySetReplyResultEnum;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * 设备控制器
 *
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/15
 */
@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/devices")
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    /**
     * 获取工作区中所有联机设备的拓扑列表。
     *
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/devices")
    public HttpResultResponse<List<DeviceDTO>> getDevices(@PathVariable("workspace_id") String workspaceId, HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);

        List<DeviceDTO> devicesList;
        if ("d14a3689-98e8-4c9f-b839-962056555149".equals(customClaim.getId())) {
            devicesList = deviceService.getDevices();
        } else {
            devicesList = deviceService.getDevicesTopoForWeb(workspaceId);
        }

        if (!devicesList.isEmpty()) {
            for (DeviceDTO deviceDTO : devicesList) {
                Optional<DeviceDTO> deviceOpt = deviceService.getDeviceBySn(deviceDTO.getChildDeviceSn());
                deviceOpt.ifPresent(deviceDTO::setChildren);
            }
        }
        return HttpResultResponse.success(devicesList);
    }

    /**
     * 将设备绑定到工作空间后，只能在web上看到设备数据。
     *
     * @param device
     * @param deviceSn
     * @return
     */
    @PostMapping("/{device_sn}/binding")
    public HttpResultResponse bindDevice(@RequestBody DeviceDTO device, @PathVariable("device_sn") String deviceSn) {
        device.setDeviceSn(deviceSn);
        boolean isUpd = deviceService.bindDevice(device);
        return isUpd ? HttpResultResponse.success() : HttpResultResponse.error();
    }

    /**
     * 根据设备sn获取设备信息。
     *
     * @param workspaceId
     * @param deviceSn
     * @return
     */
    @GetMapping("/{workspace_id}/devices/{device_sn}")
    public HttpResultResponse getDevice(@PathVariable("workspace_id") String workspaceId,
                                        @PathVariable("device_sn") String deviceSn) {
        Optional<DeviceDTO> deviceOpt = deviceService.getDeviceBySn(deviceSn);
        return deviceOpt.isEmpty() ? HttpResultResponse.error("找不到设备。") : HttpResultResponse.success(deviceOpt.get());
    }

    /**
     * 在一个工作区中获取绑定设备列表。
     *
     * @param workspaceId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/{workspace_id}/devices/bound")
    public HttpResultResponse<PaginationData<DeviceDTO>> getBoundDevicesWithDomain(
            @PathVariable("workspace_id") String workspaceId, Integer domain,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(value = "page_size", defaultValue = "50") Long pageSize) {
        PaginationData<DeviceDTO> devices = deviceService.getBoundDevicesWithDomain(workspaceId, page, pageSize, domain);

        return HttpResultResponse.success(devices);
    }

    /**
     * 正在删除设备的绑定状态。
     *
     * @param deviceSn
     * @return
     */
    @DeleteMapping("/{device_sn}/unbinding")
    public HttpResultResponse unbindingDevice(@PathVariable("device_sn") String deviceSn) {
        deviceService.unbindDevice(deviceSn);
        return HttpResultResponse.success();
    }

    /**
     * 更新设备信息。
     *
     * @param device
     * @param workspaceId
     * @param deviceSn
     * @return
     */
    @PutMapping("/{workspace_id}/devices/{device_sn}")
    public HttpResultResponse updateDevice(@RequestBody DeviceDTO device,
                                           @PathVariable("workspace_id") String workspaceId,
                                           @PathVariable("device_sn") String deviceSn) {
        device.setDeviceSn(deviceSn);
        return deviceService.updateDevice(device) ? HttpResultResponse.success() : HttpResultResponse.error();
    }

    /**
     * 提供离线固件升级任务。
     *
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    @PostMapping("/{workspace_id}/devices/ota")
    public HttpResultResponse createOtaJob(@PathVariable("workspace_id") String workspaceId,
                                           @RequestBody List<DeviceFirmwareUpgradeDTO> upgradeDTOS) {
        return deviceService.createDeviceOtaJob(workspaceId, upgradeDTOS);
    }

    /**
     * 设置无人机的属性参数。
     *
     * @param workspaceId
     * @param dockSn
     * @param param
     * @return
     */
    @PutMapping("/{workspace_id}/devices/{device_sn}/property")
    public HttpResultResponse devicePropertySet(@PathVariable("workspace_id") String workspaceId,
                                                @PathVariable("device_sn") String dockSn,
                                                @RequestBody JsonNode param) {
        if (param.size() != 1) {
            return HttpResultResponse.error(CloudSDKErrorEnum.INVALID_PARAMETER);
        }

        int result = deviceService.devicePropertySet(workspaceId, dockSn, param);
        return PropertySetReplyResultEnum.SUCCESS.getResult() == result ?
                HttpResultResponse.success() : HttpResultResponse.error(result, String.valueOf(result));
    }
}