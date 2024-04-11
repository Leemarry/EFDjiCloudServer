package com.efuav.manage.model.dto;

import com.efuav.sdk.cloudapi.device.VideoId;
import com.efuav.sdk.cloudapi.livestream.LensChangeVideoTypeEnum;
import com.efuav.sdk.cloudapi.livestream.UrlTypeEnum;
import com.efuav.sdk.cloudapi.livestream.VideoQualityEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Receive live parameters.
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/22
 */
@Data
public class LiveTypeDTO {

    @JsonProperty("url_type")
    private UrlTypeEnum urlType;

    @JsonProperty("video_id")
    private VideoId videoId;

    @JsonProperty("video_quality")
    private VideoQualityEnum videoQuality;

    private LensChangeVideoTypeEnum videoType;

}