package com.efuav.map.model.param;

import com.efuav.map.model.dto.FlightAreaContent;
import com.efuav.sdk.cloudapi.flightarea.GeofenceTypeEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Data
public class PostFlightAreaParam {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private GeofenceTypeEnum type;

    @NotNull
    @Valid
    private FlightAreaContent content;
}
