package com.efuav.map.service;

import com.efuav.map.model.dto.FlightAreaPropertyDTO;
import com.efuav.map.model.dto.FlightAreaPropertyUpdate;

import java.util.List;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
public interface IFlightAreaPropertyServices {

    List<FlightAreaPropertyDTO> getPropertyByElementIds(List<String> elementIds);

    Integer saveProperty(FlightAreaPropertyDTO property);

    Integer deleteProperty(String elementId);

    Integer updateProperty(FlightAreaPropertyUpdate property);
}
