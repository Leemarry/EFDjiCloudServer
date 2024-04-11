package com.efuav.map.model.dto;

import com.efuav.map.model.enums.FlightAreaOpertaionEnum;
import com.efuav.sdk.cloudapi.flightarea.GeofenceTypeEnum;
import lombok.*;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FlightAreaWs {

    private FlightAreaOpertaionEnum operation;

    private String areaId;

    private String name;

    private GeofenceTypeEnum type;

    private FlightAreaContent content;

    private Boolean status;

    private String username;

    private Long createTime;

    private Long updateTime;

}
