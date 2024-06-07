package com.efuav.sdk.cloudapi.wayline;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public class ExecutableConditions {

    /**
     * 存储容量
     * 可以执行任务的DJI码头或飞机的最小存储容量。单位：MB。
     * 如果存储容量不满足“storage_capacity”，则任务执行将失败。
     */
    @NotNull
    @Min(0)
    private Integer storageCapacity;

    public ExecutableConditions() {
    }

    @Override
    public String toString() {
        return "ExecutableConditions{" +
                "storageCapacity=" + storageCapacity +
                '}';
    }

    public Integer getStorageCapacity() {
        return storageCapacity;
    }

    public ExecutableConditions setStorageCapacity(Integer storageCapacity) {
        this.storageCapacity = storageCapacity;
        return this;
    }
}