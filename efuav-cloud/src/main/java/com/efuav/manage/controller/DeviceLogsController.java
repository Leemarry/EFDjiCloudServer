package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.DeviceLogsDTO;
import com.efuav.manage.model.param.DeviceLogsCreateParam;
import com.efuav.manage.model.param.DeviceLogsGetParam;
import com.efuav.manage.model.param.DeviceLogsQueryParam;
import com.efuav.manage.service.IDeviceLogsService;
import com.efuav.sdk.cloudapi.log.FileUploadUpdateRequest;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/7
 */
@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
public class DeviceLogsController {

    @Autowired
    private IDeviceLogsService deviceLogsService;

    /**
     * 根据查询参数进行分页，得到设备上传日志列表。
     * @param workspaceId
     * @param deviceSn
     * @param param
     * @return
     */
    @GetMapping("/{workspace_id}/devices/{device_sn}/logs-uploaded")
    public HttpResultResponse getUploadedLogs(DeviceLogsQueryParam param, @PathVariable("workspace_id") String workspaceId,
                                              @PathVariable("device_sn") String deviceSn) {
        PaginationData<DeviceLogsDTO> data = deviceLogsService.getUploadedLogs(deviceSn, param);
        return HttpResultResponse.success(data);
    }

    /**
     * 获取可以实时上传的日志文件列表。
     * @param workspaceId
     * @param deviceSn
     * @param param
     * @return
     */
    @GetMapping("/{workspace_id}/devices/{device_sn}/logs")
    public HttpResultResponse getLogsBySn(@PathVariable("workspace_id") String workspaceId,
                                          @PathVariable("device_sn") String deviceSn,
                                          DeviceLogsGetParam param) {
        return deviceLogsService.getRealTimeLogs(deviceSn, param.getDomainList());
    }

    /**
     * 向网关发起日志上传请求。
     * @return
     */
    @PostMapping("/{workspace_id}/devices/{device_sn}/logs")
    public HttpResultResponse uploadLogs(@PathVariable("workspace_id") String workspaceId,
                                         @PathVariable("device_sn") String deviceSn,
                                         HttpServletRequest request, @RequestBody DeviceLogsCreateParam param) {

        CustomClaim customClaim = (CustomClaim)request.getAttribute(TOKEN_CLAIM);

        return deviceLogsService.pushFileUpload(customClaim.getUsername(), deviceSn, param);
    }

    /**
     * 取消日志文件上载。
     * @return
     */
    @DeleteMapping("/{workspace_id}/devices/{device_sn}/logs")
    public HttpResultResponse cancelUploadedLogs(@PathVariable("workspace_id") String workspaceId,
                                                 @PathVariable("device_sn") String deviceSn,
                                                 @RequestBody FileUploadUpdateRequest param) {

        return deviceLogsService.pushUpdateFile(deviceSn, param);
    }

    /**
     * 删除上传历史记录。
     * @return
     */
    @DeleteMapping("/{workspace_id}/devices/{device_sn}/logs/{logs_id}")
    public HttpResultResponse deleteUploadedLogs(@PathVariable("workspace_id") String workspaceId,
                                                 @PathVariable("device_sn") String deviceSn,
                                                 @PathVariable("logs_id") String logsId) {
        deviceLogsService.deleteLogs(deviceSn, logsId);
        return HttpResultResponse.success();
    }
    /**
     * 根据路线文件id查询文件的下载地址，
     * 并直接重定向到此地址进行下载。
     * @param workspaceId
     * @param fileId
     * @param logsId
     * @param response
     */
    @GetMapping("/{workspace_id}/logs/{logs_id}/url/{file_id}")
    public HttpResultResponse getFileUrl(@PathVariable(name = "workspace_id") String workspaceId,
                                         @PathVariable(name = "file_id") String fileId,
                                         @PathVariable(name = "logs_id") String logsId, HttpServletResponse response) {

        try {
            URL url = deviceLogsService.getLogsFileUrl(logsId, fileId);
            return HttpResultResponse.success(url.toString());
        } catch (Exception e) {
            log.error("Failed to get the logs file download address.");
            e.printStackTrace();
        }
        return HttpResultResponse.error("Failed to get the logs file download address.");
    }
}
