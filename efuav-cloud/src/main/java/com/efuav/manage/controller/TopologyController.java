package com.efuav.manage.controller;

import com.efuav.manage.service.ITopologyService;
import com.efuav.sdk.cloudapi.tsa.TopologyList;
import com.efuav.sdk.cloudapi.tsa.TopologyResponse;
import com.efuav.sdk.cloudapi.tsa.api.IHttpTsaService;
import com.efuav.sdk.common.HttpResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
@RestController
public class TopologyController implements IHttpTsaService {

    @Autowired
    private ITopologyService topologyService;


    /**
     * 获取当前用户工作空间中所有设备的拓扑列表，以便进行试点显示。
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse<TopologyResponse> obtainDeviceTopologyList(String workspaceId, HttpServletRequest req, HttpServletResponse rsp) {
        List<TopologyList> topologyList = topologyService.getDeviceTopology(workspaceId);
        return HttpResultResponse.success(new TopologyResponse().setList(topologyList));
    }
}
