package com.efuav.wayline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.efuav.component.oss.model.OssConfiguration;
import com.efuav.component.oss.service.impl.OssServiceContext;
import com.efuav.wayline.dao.IWaylineFileMapper;
import com.efuav.wayline.model.dto.KmzFileProperties;
import com.efuav.wayline.model.dto.WaylineFileDTO;
import com.efuav.wayline.model.entity.WaylineFileEntity;
import com.efuav.wayline.service.IWaylineFileService;
import com.efuav.sdk.cloudapi.device.DeviceDomainEnum;
import com.efuav.sdk.cloudapi.device.DeviceEnum;
import com.efuav.sdk.cloudapi.device.DeviceSubTypeEnum;
import com.efuav.sdk.cloudapi.device.DeviceTypeEnum;
import com.efuav.sdk.cloudapi.wayline.GetWaylineListRequest;
import com.efuav.sdk.cloudapi.wayline.GetWaylineListResponse;
import com.efuav.sdk.cloudapi.wayline.WaylineTypeEnum;
import com.efuav.sdk.common.Pagination;
import com.efuav.sdk.common.PaginationData;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.efuav.wayline.model.dto.KmzFileProperties.WAYLINE_FILE_SUFFIX;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Service
@Transactional
public class WaylineFileServiceImpl implements IWaylineFileService {

    @Autowired
    private IWaylineFileMapper mapper;

    @Autowired
    private OssServiceContext ossService;

    @Override
    public PaginationData<GetWaylineListResponse> getWaylinesByParam(String workspaceId, GetWaylineListRequest param) {
        // 分页查询
        Page<WaylineFileEntity> page = mapper.selectPage(
                new Page<WaylineFileEntity>(param.getPage(), param.getPageSize()),
                new LambdaQueryWrapper<WaylineFileEntity>()
                        .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                        .eq(Objects.nonNull(param.getFavorited()), WaylineFileEntity::getFavorited, param.getFavorited())
                        .and(param.getTemplateType() != null, wrapper -> {
                            for (WaylineTypeEnum type : param.getTemplateType()) {
                                wrapper.like(WaylineFileEntity::getTemplateTypes, type.getValue()).or();
                            }
                        })
                        .and(param.getPayloadModelKey() != null, wrapper -> {
                            for (DeviceEnum type : param.getPayloadModelKey()) {
                                wrapper.like(WaylineFileEntity::getPayloadModelKeys, type.getType()).or();
                            }
                        })
                        .and(param.getDroneModelKeys() != null, wrapper -> {
                            for (DeviceEnum type : param.getDroneModelKeys()) {
                                wrapper.eq(WaylineFileEntity::getDroneModelKey, type.getType()).or();
                            }
                        })
                        .like(Objects.nonNull(param.getKey()), WaylineFileEntity::getName, param.getKey())
                        // 存在SQL注入的风险
                        .last(Objects.nonNull(param.getOrderBy()), " order by " + param.getOrderBy().toString()));

        // 将分页查询的结果包装到自定义分页对象中。
        List<GetWaylineListResponse> records = page.getRecords()
                .stream()
                .map(this::entityConvertToDTO)
                .collect(Collectors.toList());

        return new PaginationData<>(records, new Pagination(page.getCurrent(), page.getSize(), page.getTotal()));
    }

    @Override
    public Optional<GetWaylineListResponse> getWaylineByWaylineId(String workspaceId, String waylineId) {
        return Optional.ofNullable(
                this.entityConvertToDTO(
                        mapper.selectOne(
                                new LambdaQueryWrapper<WaylineFileEntity>()
                                        .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                                        .eq(WaylineFileEntity::getWaylineId, waylineId))));
    }

    @Override
    public URL getObjectUrl(String workspaceId, String waylineId) throws SQLException {
        Optional<GetWaylineListResponse> waylineOpt = this.getWaylineByWaylineId(workspaceId, waylineId);
        if (waylineOpt.isEmpty()) {
            throw new SQLException(waylineId + " 不存在。");
        }
        return ossService.getObjectUrl(OssConfiguration.bucket, waylineOpt.get().getObjectKey());
    }

    @Override
    public Integer saveWaylineFile(String workspaceId, WaylineFileDTO metadata) {
        WaylineFileEntity file = this.dtoConvertToEntity(metadata);
        file.setWaylineId(UUID.randomUUID().toString());
        file.setWorkspaceId(workspaceId);

        if (!StringUtils.hasText(file.getSign())) {
            try (InputStream object = ossService.getObject(OssConfiguration.bucket, metadata.getObjectKey())) {
                if (object.available() == 0) {
                    throw new RuntimeException("这个文件 " + metadata.getObjectKey() +
                            " 储存桶中不存在[" + OssConfiguration.bucket + "].");
                }
                file.setSign(DigestUtils.md5DigestAsHex(object));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int insertId = mapper.insert(file);
        return insertId > 0 ? file.getId() : insertId;
    }

    @Override
    public Boolean markFavorite(String workspaceId, List<String> waylineIds, Boolean isFavorite) {
        if (waylineIds.isEmpty()) {
            return false;
        }
        if (isFavorite == null) {
            return true;
        }
        return mapper.update(null, new LambdaUpdateWrapper<WaylineFileEntity>()
                .set(WaylineFileEntity::getFavorited, isFavorite)
                .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                .in(WaylineFileEntity::getWaylineId, waylineIds)) > 0;
    }

    @Override
    public List<String> getDuplicateNames(String workspaceId, List<String> names) {
        return mapper.selectList(new LambdaQueryWrapper<WaylineFileEntity>()
                .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                .in(WaylineFileEntity::getName, names))
                .stream()
                .map(WaylineFileEntity::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteByWaylineId(String workspaceId, String waylineId) {
        Optional<GetWaylineListResponse> waylineOpt = this.getWaylineByWaylineId(workspaceId, waylineId);
        if (waylineOpt.isEmpty()) {
            return true;
        }
        GetWaylineListResponse wayline = waylineOpt.get();
        boolean isDel = mapper.delete(new LambdaUpdateWrapper<WaylineFileEntity>()
                .eq(WaylineFileEntity::getWorkspaceId, workspaceId)
                .eq(WaylineFileEntity::getWaylineId, waylineId))
                > 0;
        if (!isDel) {
            return false;
        }
        return ossService.deleteObject(OssConfiguration.bucket, wayline.getObjectKey());
    }

    @Override
    public void importKmzFile(MultipartFile file, String workspaceId, String creator) {
        Optional<WaylineFileDTO> waylineFileOpt = validKmzFile(file);
        if (waylineFileOpt.isEmpty()) {
            throw new RuntimeException("文件格式不正确。");
        }

        try {
            WaylineFileDTO waylineFile = waylineFileOpt.get();
            waylineFile.setUsername(creator);

            ossService.putObject(OssConfiguration.bucket, waylineFile.getObjectKey(), file.getInputStream());
            this.saveWaylineFile(workspaceId, waylineFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<WaylineFileDTO> validKmzFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (Objects.nonNull(filename) && !filename.endsWith(WAYLINE_FILE_SUFFIX)) {
            throw new RuntimeException("文件格式不正确。");
        }
        try (ZipInputStream unzipFile = new ZipInputStream(file.getInputStream(), StandardCharsets.UTF_8)) {

            ZipEntry nextEntry = unzipFile.getNextEntry();
            while (Objects.nonNull(nextEntry)) {
                boolean isWaylines = (KmzFileProperties.FILE_DIR_FIRST + "/" + KmzFileProperties.FILE_DIR_SECOND_TEMPLATE).equals(nextEntry.getName());
                if (!isWaylines) {
                    nextEntry = unzipFile.getNextEntry();
                    continue;
                }
                SAXReader reader = new SAXReader();
                Document document = reader.read(unzipFile);
                if (!StandardCharsets.UTF_8.name().equals(document.getXMLEncoding())) {
                    throw new RuntimeException("文件编码格式不正确。");
                }

                Node droneNode = document.selectSingleNode("//" + KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_DRONE_INFO);
                Node payloadNode = document.selectSingleNode("//" + KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_PAYLOAD_INFO);
                if (Objects.isNull(droneNode) || Objects.isNull(payloadNode)) {
                    throw new RuntimeException("文件格式不正确。");
                }

                DeviceTypeEnum type = DeviceTypeEnum.find(Integer.parseInt(droneNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_DRONE_ENUM_VALUE)));
                DeviceSubTypeEnum subType = DeviceSubTypeEnum.find(Integer.parseInt(droneNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_DRONE_SUB_ENUM_VALUE)));
                DeviceTypeEnum payloadType = DeviceTypeEnum.find(Integer.parseInt(payloadNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_PAYLOAD_ENUM_VALUE)));
                DeviceSubTypeEnum payloadSubType = DeviceSubTypeEnum.find(Integer.parseInt(payloadNode.valueOf(KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_PAYLOAD_SUB_ENUM_VALUE)));
                String templateType = document.valueOf("//" + KmzFileProperties.TAG_WPML_PREFIX + KmzFileProperties.TAG_TEMPLATE_TYPE);

                return Optional.of(WaylineFileDTO.builder()
                        .droneModelKey(DeviceEnum.find(DeviceDomainEnum.DRONE, type, subType).getDevice())
                        .payloadModelKeys(List.of(DeviceEnum.find(DeviceDomainEnum.PAYLOAD, payloadType, payloadSubType).getDevice()))
                        .objectKey(OssConfiguration.objectDirPrefix + File.separator + filename)
                        .name(filename.substring(0, filename.lastIndexOf(WAYLINE_FILE_SUFFIX)))
                        .sign(DigestUtils.md5DigestAsHex(file.getInputStream()))
                        .templateTypes(List.of(WaylineTypeEnum.find(templateType).getValue()))
                        .build());
            }

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 将数据库实体对象转换为航线数据传输对象。
     *
     * @param entity
     * @return
     */
    private GetWaylineListResponse entityConvertToDTO(WaylineFileEntity entity) {
        if (entity == null) {
            return null;
        }
        return new GetWaylineListResponse()
                .setDroneModelKey(DeviceEnum.find(entity.getDroneModelKey()))
                .setFavorited(entity.getFavorited())
                .setName(entity.getName())
                .setPayloadModelKeys(entity.getPayloadModelKeys() != null ?
                        Arrays.stream(entity.getPayloadModelKeys().split(",")).map(DeviceEnum::find).collect(Collectors.toList()) : null)
                .setTemplateTypes(Arrays.stream(entity.getTemplateTypes().split(","))
                        .map(Integer::parseInt).map(WaylineTypeEnum::find)
                        .collect(Collectors.toList()))
                .setUsername(entity.getUsername())
                .setObjectKey(entity.getObjectKey())
                .setSign(entity.getSign())
                .setUpdateTime(entity.getUpdateTime())
                .setCreateTime(entity.getCreateTime())
                .setId(entity.getWaylineId());

    }

    /**
     * 将接收到的航线对象转换为数据库实体对象。
     *
     * @param file
     * @return
     */
    private WaylineFileEntity dtoConvertToEntity(WaylineFileDTO file) {
        WaylineFileEntity.WaylineFileEntityBuilder builder = WaylineFileEntity.builder();

        if (file != null) {
            builder.droneModelKey(file.getDroneModelKey())
                    .name(file.getName())
                    .username(file.getUsername())
                    .objectKey(file.getObjectKey())
                    // 用“，”分隔多个有效载荷数据。
                    .payloadModelKeys(String.join(",", file.getPayloadModelKeys()))
                    .templateTypes(file.getTemplateTypes().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .favorited(file.getFavorited())
                    .sign(file.getSign())
                    .build();
        }

        return builder.build();
    }
}
