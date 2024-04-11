package com.efuav.wayline.model.dto;

import com.efuav.sdk.cloudapi.wayline.ExecutableConditions;
import com.efuav.sdk.cloudapi.wayline.ReadyConditions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaylineTaskConditionDTO {

    private ReadyConditions readyConditions;

    private ExecutableConditions executableConditions;
}
