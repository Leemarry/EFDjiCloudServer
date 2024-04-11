package com.efuav.media.controller;

import com.efuav.media.model.MediaFileDTO;
import com.efuav.media.service.IFileService;
import com.efuav.sdk.common.HttpResultResponse;
import com.efuav.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@RestController
@RequestMapping("${url.media.prefix}${url.media.version}/files")
public class FileController {

    @Autowired
    private IFileService fileService;

    /**
     * 根据工作区id获取有关此工作区中所有媒体文件的信息。
     * @param workspaceId
     * @return
     */
    @GetMapping("/{workspace_id}/files")
    public HttpResultResponse<PaginationData<MediaFileDTO>> getFilesList(@RequestParam(defaultValue = "1") Long page,
                                                                         @RequestParam(name = "page_size", defaultValue = "10") Long pageSize,
                                                                         @PathVariable(name = "workspace_id") String workspaceId) {
        PaginationData<MediaFileDTO> filesList = fileService.getMediaFilesPaginationByWorkspaceId(workspaceId, page, pageSize);
        return HttpResultResponse.success(filesList);
    }

    /**
     *根据媒体文件id查询文件的下载地址，
     *并直接重定向到此地址进行下载。
     * @param workspaceId
     * @param fileId
     * @param response
     */
    @GetMapping("/{workspace_id}/file/{file_id}/url")
    public void getFileUrl(@PathVariable(name = "workspace_id") String workspaceId,
                           @PathVariable(name = "file_id") String fileId, HttpServletResponse response) {

        try {
            URL url = fileService.getObjectUrl(workspaceId, fileId);
            response.sendRedirect(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
