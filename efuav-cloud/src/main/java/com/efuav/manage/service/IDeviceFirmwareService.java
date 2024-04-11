package com.efuav.manage.service;

import com.efuav.manage.model.dto.DeviceFirmwareDTO;
import com.efuav.manage.model.dto.DeviceFirmwareNoteDTO;
import com.efuav.manage.model.dto.DeviceFirmwareUpgradeDTO;
import com.efuav.manage.model.param.DeviceFirmwareQueryParam;
import com.efuav.manage.model.param.DeviceFirmwareUploadParam;
import com.efuav.sdk.cloudapi.firmware.OtaCreateDevice;
import com.efuav.sdk.common.PaginationData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
public interface IDeviceFirmwareService {

    /**
     * 根据设备型号和固件版本查询特定的固件信息。
     *
     * @param workspaceId
     * @param deviceName
     * @param version
     * @return
     */
    Optional<DeviceFirmwareDTO> getFirmware(String workspaceId, String deviceName, String version);

    /**
     * 获取此设备型号的最新固件发布说明。
     * @param deviceName
     * @return
     */
    Optional<DeviceFirmwareNoteDTO> getLatestFirmwareReleaseNote(String deviceName);

    /**
     * 获取设备需要更新的固件信息。
     *
     * @param workspaceId
     * @param upgradeDTOS
     * @return
     */
    List<OtaCreateDevice> getDeviceOtaFirmware(String workspaceId, List<DeviceFirmwareUpgradeDTO> upgradeDTOS);

    /**
     * 按页面查询固件版本信息。
     *
     * @param workspaceId
     * @param param
     * @return
     */
    PaginationData<DeviceFirmwareDTO> getAllFirmwarePagination(String workspaceId, DeviceFirmwareQueryParam param);

    /**
     * 基于md5检查文件是否存在。
     *
     * @param workspaceId
     * @param fileMd5
     * @return
     */
    Boolean checkFileExist(String workspaceId, String fileMd5);

    /**
     * 导入用于设备升级的固件文件。
     * @param workspaceId
     * @param creator
     * @param param
     * @param file
     */
    void importFirmwareFile(String workspaceId, String creator, DeviceFirmwareUploadParam param, MultipartFile file);

    /**
     * 保存固件的文件信息。
     * @param firmware
     * @param deviceNames
     */
    void saveFirmwareInfo(DeviceFirmwareDTO firmware, List<String> deviceNames);

    /**
     * 更新固件的文件信息。
     * @param firmware
     */
    void updateFirmwareInfo(DeviceFirmwareDTO firmware);
}
