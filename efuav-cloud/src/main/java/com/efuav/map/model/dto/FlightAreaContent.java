package com.efuav.map.model.dto;

import com.efuav.sdk.cloudapi.map.ElementGeometryType;
import com.efuav.sdk.cloudapi.map.ElementProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightAreaContent {

    @NotNull
    @Valid
    private ElementProperty properties;

    @NotNull
    @Valid
    private ElementGeometryType geometry;

}
