package com.efuav.sdk.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 当需要分页显示时，数据响应的格式。
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Schema(description = "Format of paged data")
public class PaginationData<T> {

    /**
     * 存储数据列表的集合。
     */
    @Schema(description = "The collection in which the data list is stored.")
    private List<T> list;

    @Schema(description = "Used for paging display. These field names cannot be changed. Because they need to be the same as the pilot.")
    private Pagination pagination;

    public PaginationData() {
    }

    public PaginationData(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "PaginationData{" +
                "list=" + list +
                ", pagination=" + pagination +
                '}';
    }

    public List<T> getList() {
        return list;
    }

    public PaginationData<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public PaginationData<T> setPagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }
}
