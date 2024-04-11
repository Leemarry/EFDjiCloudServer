package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.DeviceFirmwareDTO;
import com.efuav.manage.model.dto.DeviceFirmwareNoteDTO;
import com.efuav.manage.model.dto.FirmwareFileProperties;
import com.efuav.manage.model.param.DeviceFirmwareQueryParam;
import com.efuav.manage.model.param.DeviceFirmwareUpdateParam;
import com.efuav.manage.model.param.DeviceFirmwareUploadParam;
import com.efuav.manage.service.IDeviceFirmwareService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
@Validated
public class DeviceFirmwareController {

    @Autowired
    private IDeviceFirmwareService service;

    /**
     * 获取此设备型号的最新固件版本信息。
     * @param deviceNames
     * @return
     */
    @GetMapping("/firmware-release-notes/latest")
    public HttpResultResponse<List<DeviceFirmwareNoteDTO>> getLatestFirmwareNote(@RequestParam("device_name") List<String> deviceNames) {

        List<DeviceFirmwareNoteDTO> releaseNotes = deviceNames.stream()
                .map(deviceName -> service.getLatestFirmwareReleaseNote(deviceName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return HttpResultResponse.success(releaseNotes);
    }

    /**
     * 根据参数查询固件信息。
     * @param workspaceId
     * @param param
     * @return
     */
    @GetMapping("/{workspace_id}/firmwares")
    public HttpResultResponse<PaginationData<DeviceFirmwareDTO>> getAllFirmwarePagination(
            @PathVariable("workspace_id") String workspaceId, @Valid DeviceFirmwareQueryParam param) {

        PaginationData<DeviceFirmwareDTO> data = service.getAllFirmwarePagination(workspaceId, param);
        return HttpResultResponse.success(data);
    }

    /**
     * 导入用于设备升级的固件文件。
     * @param request
     * @param workspaceId
     * @param file
     * @param param
     * @return
     */
    @PostMapping("/{workspace_id}/firmwares/file/upload")
    public HttpResultResponse importFirmwareFile(HttpServletRequest request, @PathVariable("workspace_id") String workspaceId,
                                                 @NotNull(message = "No file received.") MultipartFile file,
                                                 @Valid DeviceFirmwareUploadParam param) {

        if (!file.getOriginalFilename().endsWith(FirmwareFileProperties.FIRMWARE_FILE_SUFFIX)) {
            return HttpResultResponse.error("The file format is incorrect.");
        }

        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);
        String creator = customClaim.getUsername();

        service.importFirmwareFile(workspaceId, creator, param, file);
        return HttpResultResponse.success();
    }

    /**
     * 更改固件可用性状态。
     * @param workspaceId
     * @param firmwareId
     * @param param
     * @return
     */
    @PutMapping("/{workspace_id}/firmwares/{firmware_id}")
    public HttpResultResponse changeFirmwareStatus(@PathVariable("workspace_id") String workspaceId,
                                                   @PathVariable("firmware_id") String firmwareId,
                                                   @Valid @RequestBody DeviceFirmwareUpdateParam param) {

        service.updateFirmwareInfo(DeviceFirmwareDTO.builder()
                .firmwareId(firmwareId).firmwareStatus(param.getStatus()).build());
        return HttpResultResponse.success();
    }


}
