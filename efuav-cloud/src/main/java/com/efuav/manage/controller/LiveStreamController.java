package com.efuav.manage.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.manage.model.dto.CapacityDeviceDTO;
import com.efuav.manage.model.dto.LiveTypeDTO;
import com.efuav.manage.service.ILiveStreamService;
import com.efuav.sdk.common.HttpResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */

@RestController
@Slf4j
@RequestMapping("${url.manage.prefix}${url.manage.version}/live")
public class LiveStreamController {

    @Autowired
    private ILiveStreamService liveStreamService;

    @Autowired
    private ObjectMapper mapper;

    /**
     * 从数据库中获取当前用户工作空间中所有无人机的实时能力数据。
     *
     * @param request
     * @return live capability
     */
    @GetMapping("/capacity")
    public HttpResultResponse<List<CapacityDeviceDTO>> getLiveCapacity(HttpServletRequest request) {
        // 获取有关当前用户的信息。
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);

        List<CapacityDeviceDTO> liveCapacity = liveStreamService.getLiveCapacity(customClaim.getWorkspaceId());

        return HttpResultResponse.success(liveCapacity);
    }

    /**
     * 根据从web端传入的参数进行直播。
     *
     * @param liveParam 直播参数。
     * @return
     */
    @PostMapping("/streams/start")
    public HttpResultResponse liveStart(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveStart(liveParam);
    }

    /**
     * 根据web端传入的参数停止直播。
     *
     * @param liveParam 直播参数。
     * @return
     */
    @PostMapping("/streams/stop")
    public HttpResultResponse liveStop(@RequestBody LiveTypeDTO liveParam) {
        if (liveParam.getVideoId() == null) {
            return HttpResultResponse.error("videoId不能为空！");
        }
        return liveStreamService.liveStop(liveParam.getVideoId());
    }

    /**
     * 根据从web端传入的参数设置直播的质量。
     *
     * @param liveParam 直播参数。
     * @return
     */
    @PostMapping("/streams/update")
    public HttpResultResponse liveSetQuality(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveSetQuality(liveParam);
    }

    @PostMapping("/streams/switch")
    public HttpResultResponse liveLensChange(@RequestBody LiveTypeDTO liveParam) {
        return liveStreamService.liveLensChange(liveParam);
    }

}