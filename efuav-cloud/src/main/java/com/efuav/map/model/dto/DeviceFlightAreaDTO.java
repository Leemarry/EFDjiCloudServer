package com.efuav.map.model.dto;

import com.efuav.sdk.cloudapi.flightarea.FlightAreaSyncReasonEnum;
import com.efuav.sdk.cloudapi.flightarea.FlightAreaSyncStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceFlightAreaDTO {

    private String deviceSn;

    private String workspaceId;

    private String fileId;

    private FlightAreaSyncStatusEnum syncStatus;

    private FlightAreaSyncReasonEnum syncCode;

    private String syncMsg;
}
