package com.efuav.wayline.service;

import com.efuav.wayline.model.dto.WaylineFileDTO;
import com.efuav.sdk.cloudapi.wayline.GetWaylineListRequest;
import com.efuav.sdk.cloudapi.wayline.GetWaylineListResponse;
import com.efuav.sdk.common.PaginationData;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
public interface IWaylineFileService {

    /**
     * 根据查询参数执行分页查询。
     *
     * @param workspaceId
     * @param param
     * @return
     */
    PaginationData<GetWaylineListResponse> getWaylinesByParam(String workspaceId, GetWaylineListRequest param);

    /**
     * 根据路线文件id查询该路线文件的信息。
     *
     * @param workspaceId
     * @param waylineId
     * @return
     */
    Optional<GetWaylineListResponse> getWaylineByWaylineId(String workspaceId, String waylineId);

    /**
     * 获取文件对象的下载地址。
     *
     * @param workspaceId
     * @param waylineId
     * @return
     */
    URL getObjectUrl(String workspaceId, String waylineId) throws SQLException;

    /**
     * 保存航线文件的基本信息。
     *
     * @param workspaceId
     * @param metadata
     * @return
     */
    Integer saveWaylineFile(String workspaceId, WaylineFileDTO metadata);

    /**
     * 根据传递的参数更新是否收集文件。
     *
     * @param workspaceId
     * @param ids         wayline id
     * @param isFavorite  Whether the wayline file is favorited or not.
     * @return
     */
    Boolean markFavorite(String workspaceId, List<String> ids, Boolean isFavorite);

    /**
     * 批量查询工作区中的重复文件名。
     *
     * @param workspaceId
     * @param names
     * @return
     */
    List<String> getDuplicateNames(String workspaceId, List<String> names);

    /**
     * 根据路线id删除路线文件。
     *
     * @param workspaceId
     * @param waylineId
     */
    Boolean deleteByWaylineId(String workspaceId, String waylineId);

    /**
     * 导入kmz航线文件。
     *
     * @param file
     * @param workspaceId
     * @param creator
     * @return
     */
    void importKmzFile(MultipartFile file, String workspaceId, String creator);
}
