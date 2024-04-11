package com.efuav.map.service;

import com.efuav.sdk.cloudapi.map.ElementCoordinate;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
public interface IElementCoordinateService {

    /**
     * 根据元素的id查询元素的所有坐标。
     * @param elementId
     * @return
     */
    List<ElementCoordinate> getCoordinateByElementId(String elementId);

    /**
     * 保存此元素的所有坐标数据。
     * @param coordinate
     * @param elementId
     * @return
     */
    Boolean saveCoordinate(List<ElementCoordinate> coordinate, String elementId);

    /**
     * 根据元素的id删除元素的所有坐标。
     * @param elementId
     * @return
     */
    Boolean deleteCoordinateByElementId(String elementId);
}
