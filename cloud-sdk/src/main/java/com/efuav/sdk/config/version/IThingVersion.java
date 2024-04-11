package com.efuav.sdk.config.version;

/**
 * @author sean
 * @version 1.7
 * @date 2023/9/7
 */
public interface IThingVersion {

    String getThingVersion();

    CloudSDKVersionEnum getCloudSDKVersion();
}
