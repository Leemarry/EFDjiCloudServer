package com.efuav.wayline.controller;

import com.efuav.common.model.CustomClaim;
import com.efuav.component.oss.service.impl.MinIOServiceImpl;
import com.efuav.wayline.model.dto.WaylineFileDTO;
import com.efuav.wayline.model.entity.template.WaylineEntity;
import com.efuav.wayline.service.IWaylineFileService;
import com.efuav.sdk.cloudapi.device.DeviceEnum;
import com.efuav.sdk.cloudapi.wayline.*;
import com.efuav.sdk.cloudapi.wayline.api.IHttpWaylineService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import com.efuav.wayline.util.WaylineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.efuav.component.AuthInterceptor.TOKEN_CLAIM;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}/workspaces")
public class WaylineFileController implements IHttpWaylineService {

    @Autowired
    private IWaylineFileService waylineFileService;

    @Autowired
    private MinIOServiceImpl minIOService;

    /**
     * 根据路线id删除工作区中的航线文件。
     *
     * @param workspaceId
     * @param waylineId
     * @return
     */
    @DeleteMapping("/{workspace_id}/waylines/{wayline_id}")
    public HttpResultResponse deleteWayline(@PathVariable(name = "workspace_id") String workspaceId,
                                            @PathVariable(name = "wayline_id") String waylineId) {
        boolean isDel = waylineFileService.deleteByWaylineId(workspaceId, waylineId);
        return isDel ? HttpResultResponse.success() : HttpResultResponse.error("未能删除航线。");
    }

    /**
     * 导入kmz航线文件。
     *
     * @param file
     * @return
     */
    @PostMapping("/{workspace_id}/waylines/file/upload")
    public HttpResultResponse importKmzFile(HttpServletRequest request, MultipartFile file) {
        if (Objects.isNull(file)) {
            return HttpResultResponse.error("未收到任何文件。");
        }
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        String workspaceId = customClaim.getWorkspaceId();
        String creator = customClaim.getUsername();
        waylineFileService.importKmzFile(file, workspaceId, creator);
        return HttpResultResponse.success();
    }

    /**
     * 新增航线任务并保存至MINIO服务器。
     *
     * @param waylineEntity 航线实体
     * @return 成功 失败
     */
    @ResponseBody
    @PostMapping("/{workspace_id}/waylines/file/addWayLine")
    public HttpResultResponse addWayLine(@RequestBody WaylineEntity waylineEntity,
                                         @PathVariable(name = "workspace_id") String workspaceId) throws FileNotFoundException {
//        File file = new File("c://");
//        FileInputStream inputStream = new FileInputStream(file);
//        minIOService.createClient();
//        minIOService.putObject("efuav", workspaceId, inputStream);
        String path = WaylineUtil.generateKmz(waylineEntity);
        if (!Objects.equals(path, "")) {
            File file = new File(path);
            try (FileInputStream inputStream = new FileInputStream(file)) {
                minIOService.createClient();
                minIOService.putObject("efuav", workspaceId, inputStream);
                //保存航线信息到数据库
//                waylineFileService
                return HttpResultResponse.success();
            } catch (IOException e) {
                e.printStackTrace();
                return HttpResultResponse.error();
            }
        } else {
            return HttpResultResponse.error();
        }
    }


    /**
     * 根据查询条件查询航线文件的基础数据。
     * pilot中的查询条件字段是固定的。
     *
     * @param request
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse<PaginationData<GetWaylineListResponse>> getWaylineList(@Valid GetWaylineListRequest request, String workspaceId, HttpServletRequest req, HttpServletResponse rsp) {
        PaginationData<GetWaylineListResponse> data = waylineFileService.getWaylinesByParam(workspaceId, request);
        return HttpResultResponse.success(data);
    }

    /**
     * 根据航线文件id查询文件的下载地址，
     * 并直接重定向到此地址进行下载。
     *
     * @param workspaceId
     * @param waylineId
     * @param req
     * @param rsp
     */
    @Override
    public void getWaylineFileDownloadAddress(String workspaceId, String waylineId, HttpServletRequest req, HttpServletResponse rsp) {
        try {
            URL url = waylineFileService.getObjectUrl(workspaceId, waylineId);
            rsp.sendRedirect(url.toString());

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据航线名称检查名称是否已存在，必须确保航线名称的唯一性。
     * 此接口将在上传路线时调用，并且必须可用。
     *
     * @param workspaceId
     * @param names
     * @return
     */
    @Override
    public HttpResultResponse<List<String>> getDuplicatedWaylineName(String workspaceId, @NotNull @Size(min = 1) List<String> names, HttpServletRequest req, HttpServletResponse rsp) {
        List<String> existNamesList = waylineFileService.getDuplicateNames(workspaceId, names);

        return HttpResultResponse.success(existNamesList);
    }

    /**
     * 当航线文件通过飞行员上传到存储服务器时，
     * 文件的基本信息是通过这个接口报告的。
     *
     * @param request
     * @param workspaceId
     * @return
     */
    @Override
    public HttpResultResponse fileUploadResultReport(String workspaceId, @Valid WaylineUploadCallbackRequest request, HttpServletRequest req, HttpServletResponse rsp) {
        CustomClaim customClaim = (CustomClaim) req.getAttribute(TOKEN_CLAIM);

        WaylineUploadCallbackMetadata metadata = request.getMetadata();

        WaylineFileDTO file = WaylineFileDTO.builder()
                .username(customClaim.getUsername())
                .objectKey(request.getObjectKey())
                .name(request.getName())
                .templateTypes(metadata.getTemplateTypes().stream().map(WaylineTypeEnum::getValue).collect(Collectors.toList()))
                .payloadModelKeys(metadata.getPayloadModelKeys().stream().map(DeviceEnum::getDevice).collect(Collectors.toList()))
                .droneModelKey(metadata.getDroneModelKey().getDevice())
                .build();

        int id = waylineFileService.saveWaylineFile(workspaceId, file);

        return id <= 0 ? HttpResultResponse.error() : HttpResultResponse.success();
    }

    /**
     * 根据航线文件id收藏路线文件。
     *
     * @param workspaceId
     * @param ids         航线文件id
     * @return
     */
    @Override
    public HttpResultResponse batchFavoritesWayline(String workspaceId, @NotNull @Size(min = 1) List<String> ids, HttpServletRequest req, HttpServletResponse rsp) {
        boolean isMark = waylineFileService.markFavorite(workspaceId, ids, true);

        return isMark ? HttpResultResponse.success() : HttpResultResponse.error();
    }

    /**
     * 根据航线文件id删除此航线文件的收藏夹。
     *
     * @param workspaceId
     * @param ids         航线文件id
     * @return
     */
    @Override
    public HttpResultResponse batchUnfavoritesWayline(String workspaceId, @NotNull @Size(min = 1) List<String> ids, HttpServletRequest req, HttpServletResponse rsp) {
        boolean isMark = waylineFileService.markFavorite(workspaceId, ids, false);

        return isMark ? HttpResultResponse.success() : HttpResultResponse.error();
    }
}
