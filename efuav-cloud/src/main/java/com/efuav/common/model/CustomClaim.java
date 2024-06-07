package com.efuav.common.model;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于在令牌中存储自定义信息的自定义声明。
 * @author sean.zhou
 * @date 2021/11/16
 * @version 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class CustomClaim {

    /**
     * 账户的user_id
     */
    private String id;

    private String username;

    @JsonAlias("user_type")
    private Integer userType;

    @JsonAlias("workspace_id")
    private String workspaceId;

    /**
     * 将自定义索赔数据类型转换为Map类型。
     * @return map
     */
    public ConcurrentHashMap<String, String> convertToMap() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(4);
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                JsonAlias annotation = field.getAnnotation(JsonAlias.class);
                field.setAccessible(true);
                // 键的值命名为下划线。
                map.put(annotation != null ? annotation.value()[0] : field.getName(),
                        field.get(this).toString());
            }
        } catch (IllegalAccessException e) {
            log.info("CustomClaim转换失败。 {}", this.toString());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将Map中的数据转换为自定义索赔对象。
     * @param claimMap
     */
    public CustomClaim (Map<String, Claim> claimMap) {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            JsonAlias annotation = field.getAnnotation(JsonAlias.class);

            Claim value = claimMap.get(annotation == null ? field.getName() : annotation.value()[0]);
            try {
                Class<?> type = field.getType();
                if (Integer.class.equals(type)) {
                    field.set(this, Integer.valueOf(value.asString()));
                    continue;
                }
                if (String.class.equals(type)) {
                    field.set(this, value.asString());
                    continue;
                }
            } catch (IllegalAccessException e) {
                log.info("声明解析失败。 {}", claimMap.toString());
                e.printStackTrace();
            }
        }
    }
}