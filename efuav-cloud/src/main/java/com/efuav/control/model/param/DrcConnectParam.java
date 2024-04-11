package com.efuav.control.model.param;

import com.efuav.component.redis.RedisConst;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
@Data
public class DrcConnectParam {

    private String clientId;

    @Range(min = 1800, max = 86400)
    private long expireSec = RedisConst.DRC_MODE_ALIVE_SECOND;
}
