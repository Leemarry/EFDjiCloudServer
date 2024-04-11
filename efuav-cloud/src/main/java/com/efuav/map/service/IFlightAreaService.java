package com.efuav.map.service;

import com.efuav.map.model.dto.FlightAreaDTO;
import com.efuav.map.model.dto.FlightAreaFileDTO;
import com.efuav.map.model.param.PostFlightAreaParam;
import com.efuav.map.model.param.PutFlightAreaParam;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/22
 */
public interface IFlightAreaService {

    Optional<FlightAreaDTO> getFlightAreaByAreaId(String areaId);

    List<FlightAreaDTO> getFlightAreaList(String workspaceId);

    void createFlightArea(String workspaceId, String username, PostFlightAreaParam param);

    void syncFlightArea(String workspaceId, List<String> deviceSns);

    FlightAreaFileDTO packageFlightArea(String workspaceId);

    void deleteFlightArea(String workspaceId, String areaId);

    void updateFlightArea(String workspaceId, String areaId, PutFlightAreaParam param);

}
