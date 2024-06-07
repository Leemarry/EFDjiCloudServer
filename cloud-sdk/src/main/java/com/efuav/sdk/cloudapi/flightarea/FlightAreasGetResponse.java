package com.efuav.sdk.cloudapi.flightarea;

import com.efuav.sdk.common.BaseModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreasGetResponse extends BaseModel {

    /**
     * 文件列表
     */
    @NotNull
    private List<@Valid FlightAreaGetFile> files;

    public FlightAreasGetResponse() {
    }

    @Override
    public String toString() {
        return "FlightAreasGetResponse{" +
                "files=" + files +
                '}';
    }

    public List<FlightAreaGetFile> getFiles() {
        return files;
    }

    public FlightAreasGetResponse setFiles(List<FlightAreaGetFile> files) {
        this.files = files;
        return this;
    }
}
