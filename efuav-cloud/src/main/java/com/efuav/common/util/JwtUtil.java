package com.efuav.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.efuav.common.model.CustomClaim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Component
public class JwtUtil {

    private static String issuer;

    private static String subject;

    private static long age;

    private static String secret;

    public static Algorithm algorithm;

    @Value("${jwt.issuer: DJI}")
    private void setIssuer(String issuer) {
        JwtUtil.issuer = issuer;
    }

    @Value("${jwt.subject: CloudApiSample}")
    private void setSubject(String subject) {
        JwtUtil.subject = subject;
    }

    @Value("${jwt.age: 86400}")
    private void setAge(long age) {
        JwtUtil.age = age * 1000;
    }

    @Value("${jwt.secret: CloudApiSample}")
    private void setSecret(String secret) {
        JwtUtil.secret = secret;
        setAlgorithm();
    }

    private void setAlgorithm() {
        JwtUtil.algorithm = Algorithm.HMAC256(secret);
    }

    private JwtUtil() {

    }

    /**
     * 根据自定义信息创建令牌。
     * @param claims 自定义信息
     * @return token
     */
    public static String createToken(Map<String, ?> claims) {
        return JwtUtil.createToken(claims, age, algorithm, subject, issuer);
    }

    /**
     *
     * @param claims
     * @param age       unit: s
     * @param algorithm
     * @param subject
     * @param issuer
     * @return
     */
    public static String createToken(Map<String, ?> claims, Long age, Algorithm algorithm, String subject, String issuer) {
        if (Objects.isNull(algorithm)) {
            throw new IllegalArgumentException();
        }

        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        // 将自定义信息添加到令牌的有效负载段。
        claims.forEach((k, v) -> {
            if (Objects.nonNull(v.getClass().getClassLoader())) {
                log.error("claim can't be set to a custom object.");
                return;
            }
            if (v instanceof Map) {
                builder.withClaim(k, (Map) v);
            } else if (v instanceof List) {
                builder.withClaim(k, (List) v);
            } else {
                builder.withClaim(k, String.valueOf(v));
            }
        });

        if (StringUtils.hasText(subject)) {
            builder.withSubject(subject);
        }

        if (StringUtils.hasText(issuer)) {
            builder.withIssuer(issuer);
        }

        if (Objects.nonNull(age)) {
            builder.withExpiresAt(new Date(now.getTime() + age));
        }

        String token = builder
                .withIssuedAt(now)
                .withNotBefore(now)
                .sign(algorithm);
        log.debug("token created. " + token);
        return token;
    }

    /**
     * 验证令牌是否有效。
     * @param token
     * @return
     * @throws TokenExpiredException
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    /**
     * 将令牌中的自定义信息解析为CustomClaim对象。
     * @param token
     * @return custom claim
     */
    public static Optional<CustomClaim> parseToken(String token) {
        DecodedJWT jwt;
        try {
            jwt = verifyToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(new CustomClaim(jwt.getClaims()));
    }
}
