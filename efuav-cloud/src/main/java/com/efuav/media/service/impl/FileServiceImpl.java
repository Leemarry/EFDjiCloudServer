package com.efuav.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efuav.component.oss.model.OssConfiguration;
import com.efuav.component.oss.service.impl.OssServiceContext;
import com.efuav.manage.model.dto.DeviceDictionaryDTO;
import com.efuav.manage.service.IDeviceDictionaryService;
import com.efuav.media.dao.IFileMapper;
import com.efuav.media.model.MediaFileDTO;
import com.efuav.media.model.MediaFileEntity;
import com.efuav.media.service.IFileService;
import com.efuav.sdk.cloudapi.device.DeviceEnum;
import com.efuav.sdk.cloudapi.media.MediaSubFileTypeEnum;
import com.efuav.sdk.cloudapi.media.MediaUploadCallbackRequest;
import com.efuav.sdk.common.Pagination;
import com.efuav.sdk.common.PaginationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/9
 */
@Service
@Transactional
public class FileServiceImpl implements IFileService {

    @Autowired
    private IFileMapper mapper;

    @Autowired
    private IDeviceDictionaryService deviceDictionaryService;

    @Autowired
    private OssServiceContext ossService;

    private Optional<MediaFileEntity> getMediaByFingerprint(String workspaceId, String fingerprint) {
        MediaFileEntity fileEntity = mapper.selectOne(new LambdaQueryWrapper<MediaFileEntity>()
                .eq(MediaFileEntity::getWorkspaceId, workspaceId)
                .eq(MediaFileEntity::getFingerprint, fingerprint));
        return Optional.ofNullable(fileEntity);
    }

    private Optional<MediaFileEntity> getMediaByFileId(String workspaceId, String fileId) {
        MediaFileEntity fileEntity = mapper.selectOne(new LambdaQueryWrapper<MediaFileEntity>()
                .eq(MediaFileEntity::getWorkspaceId, workspaceId)
                .eq(MediaFileEntity::getFileId, fileId));
        return Optional.ofNullable(fileEntity);
    }

    @Override
    public Boolean checkExist(String workspaceId, String fingerprint) {
        return this.getMediaByFingerprint(workspaceId, fingerprint).isPresent();
    }

    @Override
    public Integer saveFile(String workspaceId, MediaUploadCallbackRequest file) {
        MediaFileEntity fileEntity = this.fileUploadConvertToEntity(file);
        fileEntity.setWorkspaceId(workspaceId);
        fileEntity.setFileId(UUID.randomUUID().toString());
        return mapper.insert(fileEntity);
    }

    @Override
    public List<MediaFileDTO> getAllFilesByWorkspaceId(String workspaceId) {
        return mapper.selectList(new LambdaQueryWrapper<MediaFileEntity>()
                .eq(MediaFileEntity::getWorkspaceId, workspaceId))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationData<MediaFileDTO> getMediaFilesPaginationByWorkspaceId(String workspaceId, long page, long pageSize) {
        Page<MediaFileEntity> pageData = mapper.selectPage(
                new Page<MediaFileEntity>(page, pageSize),
                new LambdaQueryWrapper<MediaFileEntity>()
                        .eq(MediaFileEntity::getWorkspaceId, workspaceId)
                        .orderByDesc(MediaFileEntity::getId));
        List<MediaFileDTO> records = pageData.getRecords()
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());

        return new PaginationData<MediaFileDTO>(records, new Pagination(pageData.getCurrent(), pageData.getSize(), pageData.getTotal()));
    }

    @Override
    public URL getObjectUrl(String workspaceId, String fileId) {
        Optional<MediaFileEntity> mediaFileOpt = getMediaByFileId(workspaceId, fileId);
        if (mediaFileOpt.isEmpty()) {
            throw new IllegalArgumentException("{} doesn't exist.");
        }

        return ossService.getObjectUrl(OssConfiguration.bucket, mediaFileOpt.get().getObjectKey());
    }

    @Override
    public List<MediaFileDTO> getFilesByWorkspaceAndJobId(String workspaceId, String jobId) {
        return mapper.selectList(new LambdaQueryWrapper<MediaFileEntity>()
                .eq(MediaFileEntity::getWorkspaceId, workspaceId)
                .eq(MediaFileEntity::getJobId, jobId))
                .stream()
                .map(this::entityConvertToDto).collect(Collectors.toList());
    }

    /**
     * 将接收到的文件对象转换为数据库实体对象。
     * @param file
     * @return
     */
    private MediaFileEntity fileUploadConvertToEntity(MediaUploadCallbackRequest file) {
        MediaFileEntity.MediaFileEntityBuilder builder = MediaFileEntity.builder();

        if (file != null) {
            builder.fileName(file.getName())
                    .filePath(file.getPath())
                    .fingerprint(file.getFingerprint())
                    .objectKey(file.getObjectKey())
                    .subFileType(Optional.ofNullable(file.getSubFileType()).map(MediaSubFileTypeEnum::getType).orElse(null))
                    .isOriginal(file.getExt().getOriginal())
                    .jobId(file.getExt().getFileGroupId())
                    .drone(file.getExt().getSn())
                    .tinnyFingerprint(file.getExt().getTinnyFingerprint())
                    .payload(file.getExt().getPayloadModelKey().getDevice());

            // domain-type-subType
            DeviceEnum payloadModelKey = file.getExt().getPayloadModelKey();
            Optional<DeviceDictionaryDTO> payloadDict = deviceDictionaryService
                    .getOneDictionaryInfoByTypeSubType(payloadModelKey.getDomain().getDomain(),
                            payloadModelKey.getType().getType(), payloadModelKey.getSubType().getSubType());
            payloadDict.ifPresent(payload -> builder.payload(payload.getDeviceName()));
        }
        return builder.build();
    }

    /**
     * 将数据库实体对象转换为文件数据传输对象。
     * @param entity
     * @return
     */
    private MediaFileDTO entityConvertToDto(MediaFileEntity entity) {
        MediaFileDTO.MediaFileDTOBuilder builder = MediaFileDTO.builder();

        if (entity != null) {
            builder.fileName(entity.getFileName())
                    .fileId(entity.getFileId())
                    .filePath(entity.getFilePath())
                    .isOriginal(entity.getIsOriginal())
                    .fingerprint(entity.getFingerprint())
                    .objectKey(entity.getObjectKey())
                    .tinnyFingerprint(entity.getTinnyFingerprint())
                    .payload(entity.getPayload())
                    .createTime(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(entity.getCreateTime()), ZoneId.systemDefault()))
                    .drone(entity.getDrone())
                    .jobId(entity.getJobId());

        }

        return builder.build();
    }

}
