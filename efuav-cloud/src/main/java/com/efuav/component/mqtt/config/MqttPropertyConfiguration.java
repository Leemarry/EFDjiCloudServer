package com.efuav.component.mqtt.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.efuav.common.util.JwtUtil;
import com.efuav.component.mqtt.model.MqttClientOptions;
import com.efuav.component.mqtt.model.MqttProtocolEnum;
import com.efuav.component.mqtt.model.MqttUseEnum;
import com.efuav.sdk.cloudapi.control.DrcModeMqttBroker;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 *
 * @author sean.zhou
 * @date 2021/11/10
 * @version 0.1
 */
@Configuration
@Data
@ConfigurationProperties
public class MqttPropertyConfiguration {

    private static Map<MqttUseEnum, MqttClientOptions> mqtt;

    public void setMqtt(Map<MqttUseEnum, MqttClientOptions> mqtt) {
        MqttPropertyConfiguration.mqtt = mqtt;
    }

    /**
     * 获取mqtt客户端的基本链接的配置选项。
     * @return
     */
    static MqttClientOptions getBasicClientOptions() {
        if (!mqtt.containsKey(MqttUseEnum.BASIC)) {
            throw new Error("请先配置基本的MQTT连接参数，否则应用程序将无法启动。");
        }
        return mqtt.get(MqttUseEnum.BASIC);
    }

    /**
     * 获取基本链接的mqtt地址。
     * @return
     */
    public static String getBasicMqttAddress() {
        return getMqttAddress(getBasicClientOptions());
    }

    /**
     * 根据不同客户端的参数拼接mqtt地址。
     * @param options
     * @return
     */
    private static String getMqttAddress(MqttClientOptions options) {
        StringBuilder addr = new StringBuilder()
                .append(options.getProtocol().getProtocolAddr())
                .append(options.getHost().trim())
                .append(":")
                .append(options.getPort());
        if ((options.getProtocol() == MqttProtocolEnum.WS || options.getProtocol() == MqttProtocolEnum.WSS)
                && StringUtils.hasText(options.getPath())) {
            addr.append(options.getPath());
        }
        return addr.toString();
    }

    /**
     * 获取drc链接的mqtt客户端的连接参数。
     * @param clientId
     * @param username
     * @param age   令牌的有效期。单位：s
     * @param map   在令牌中添加的自定义数据。
     * @return
     */
    public static DrcModeMqttBroker getMqttBrokerWithDrc(String clientId, String username, Long age, Map<String, ?> map) {
        if (!mqtt.containsKey(MqttUseEnum.DRC)) {
            throw new RuntimeException("请先在后端配置文件中配置MQTT的DRC链接参数。");
        }
        Algorithm algorithm = JwtUtil.algorithm;

        String token = JwtUtil.createToken(map, age, algorithm, null, null);

        return new DrcModeMqttBroker()
                .setAddress(getMqttAddress(mqtt.get(MqttUseEnum.DRC)))
                .setUsername(username)
                .setClientId(clientId)
                .setExpireTime(System.currentTimeMillis() / 1000 + age)
                .setPassword(token)
                .setEnableTls(false);
    }


    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttClientOptions customizeOptions = getBasicClientOptions();
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{ getBasicMqttAddress() });
        mqttConnectOptions.setUserName(customizeOptions.getUsername());
        mqttConnectOptions.setPassword(StringUtils.hasText(customizeOptions.getPassword()) ?
                customizeOptions.getPassword().toCharArray() : new char[0]);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setKeepAliveInterval(10);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }
}
