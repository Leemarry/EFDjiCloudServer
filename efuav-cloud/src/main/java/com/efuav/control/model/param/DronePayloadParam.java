package com.efuav.control.model.param;

import com.efuav.sdk.cloudapi.control.CameraTypeEnum;
import com.efuav.sdk.cloudapi.control.GimbalResetModeEnum;
import com.efuav.sdk.cloudapi.device.CameraModeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
@Data
public class DronePayloadParam {

    @Pattern(regexp = "\\d+-\\d+-\\d+")
    @NotNull
    private String payloadIndex;

    private CameraTypeEnum cameraType;

    @Range(min = 2, max = 200)
    private Float zoomFactor;

    private CameraModeEnum cameraMode;

    /**
     * true：锁定万向节，万向节和无人机一起旋转。
     * false：只有万向节旋转，但无人机不旋转。
     */
    private Boolean locked;

    private Double pitchSpeed;

    /**
     * 仅在锁定时有效为false。
     */
    private Double yawSpeed;

    /**
     * 左上角作为中心点
     */
    @Range(min = 0, max = 1)
    private Double x;

    @Range(min = 0, max = 1)
    private Double y;

    private GimbalResetModeEnum resetMode;
}
