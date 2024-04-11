package com.efuav.manage.service;

import com.efuav.manage.model.dto.CapacityDeviceDTO;
import com.efuav.manage.model.dto.LiveTypeDTO;
import com.efuav.sdk.cloudapi.device.VideoId;
import com.efuav.sdk.common.HttpResultResponse;

import java.util.List;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
public interface ILiveStreamService {

    /**
     * 获取所有可以在此工作区进行直播的无人机数据。
     *
     * @param workspaceId
     * @return
     */
    List<CapacityDeviceDTO> getLiveCapacity(String workspaceId);

    /**
     * 通过发布mqtt消息启动直播。
     *
     * @param liveParam 按需所需的参数。
     * @return
     */
    HttpResultResponse liveStart(LiveTypeDTO liveParam);

    /**
     * 通过发布mqtt消息来停止直播。
     *
     * @param videoId
     * @return
     */
    HttpResultResponse liveStop(VideoId videoId);

    /**
     * Readjust 通过发布mqtt消息来提高直播的清晰度。
     *
     * @param liveParam
     * @return
     */
    HttpResultResponse liveSetQuality(LiveTypeDTO liveParam);

    /**
     * 在直播过程中切换设备的镜头。
     *
     * @param liveParam
     * @return
     */
    HttpResultResponse liveLensChange(LiveTypeDTO liveParam);
}
