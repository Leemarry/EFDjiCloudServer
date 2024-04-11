package com.efuav.manage.model.receiver;

import com.efuav.sdk.cloudapi.log.FileUploadProgressExt;
import lombok.Data;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Data
public class OutputLogsProgressReceiver {

    private FileUploadProgressExt ext;

    private String status;
}
