package com.efuav.sdk.cloudapi.storage.api;

import com.efuav.sdk.cloudapi.storage.StsCredentialsResponse;
import com.efuav.sdk.common.HttpResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/29
 */
public interface IHttpStorageService {

    String PREFIX = "storage/api/v1";

    /**
     * 获取用于在DJI Pilot中上传媒体和路线的临时凭据。
     *
     * @param workspaceId workspace id
     * @param req
     * @param rsp
     * @return
     */
    @Operation(summary = "Get STS Token", description = "Get temporary credentials for uploading the media and wayline in DJI Pilot.",
            parameters = {
                    @Parameter(name = "workspace_id", description = "workspace id", schema = @Schema(format = "uuid"))
            })
    @PostMapping(PREFIX + "/workspaces/{workspace_id}/sts")
    HttpResultResponse<StsCredentialsResponse> getTemporaryCredential(
            @PathVariable(name = "workspace_id") String workspaceId,
            HttpServletRequest req, HttpServletResponse rsp);

}
