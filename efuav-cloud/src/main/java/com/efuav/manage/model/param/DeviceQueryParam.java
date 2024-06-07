package com.efuav.manage.model.param;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 设备查询字段的对象。
 *
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@Data
@Builder
public class DeviceQueryParam {

    private String deviceSn;

    private String workspaceId;

    private Integer deviceType;

    private Integer subType;

    private List<Integer> domains;

    private String childSn;

    private Boolean boundStatus;

    private boolean orderBy;

    private boolean isAsc;
}