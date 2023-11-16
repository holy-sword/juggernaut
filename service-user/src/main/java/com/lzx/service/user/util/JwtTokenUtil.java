package com.lzx.service.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

/**
 * @author lzx
 * @since 2018/12/20
 */
public class JwtTokenUtil {

    /**
     * 使用的加密算法
     */
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    /**
     * 加密时秘钥需使用 base64 编码
     */
    public static final byte[] HS256_KEY = Base64.getEncoder().encode("service-user-hs256-key-012345678901234567890123456789".getBytes());
    /**
     * 默认过期时间（7天）
     */
    public static final int DEFAULT_EXPIRE = 7 * 24 * 60 * 60;
    /**
     * jwt存储用户ID的key
     */
    public static final String USER_ID = "X-User-Id";
    /**
     * jwt存储用户名的key
     */
    public static final String NAME = "X-User-Name";
    /**
     * jwt存储用户权限的key
     */
    public static final String ROLE = "X-User-Role";
    /**
     * jwt规范中的标准前缀 Bearer
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 根据用户生成token
     */
    public static String generateToken(UserInfo user) {
        return generateToken(user, HS256_KEY, DEFAULT_EXPIRE);
    }

    /**
     * 密钥加密token
     *
     * @param user   用户
     * @param key    加密秘钥（base64 编码）
     * @param expire 过期时间（单位秒）
     */
    public static String generateToken(UserInfo user, byte[] key, int expire) {
        return Jwts.builder()
                .setSubject(user.getLoginName())
                .claim(USER_ID, user.getId())
                .claim(NAME, user.getName())
                .claim(ROLE, user.getRole())
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(expire).atZone(ZoneOffset.systemDefault()).toInstant()))
                .signWith(Keys.hmacShaKeyFor(key), signatureAlgorithm)
                .compact();
    }


    /**
     * 公钥解析token
     *
     * @param token json web token
     * @return the {@link Jws Jws} instance that reflects the specified compact Claims JWS string.
     */
    public static Jws<Claims> parserToken(String token) {
        return Jwts.parser().setSigningKey(HS256_KEY).build().parseClaimsJws(token);
    }


    /**
     * 获取token中的用户信息
     *
     * @param token json web token
     */
    public static UserInfo getUserFromToken(String token) {
        Jws<Claims> claimsJws = parserToken(token);
        Claims body = claimsJws.getBody();
        UserInfo user = new UserInfo();
        //数字没有超过int表示范围时会解析成int
        user.setId(Long.valueOf(body.get(USER_ID).toString()));
        user.setLoginName(body.getSubject());
        user.setName(String.valueOf(body.get(NAME)));
        user.setRole(String.valueOf(body.get(ROLE)));
        return user;
    }


    public static class UserInfo {
        private Long id;
        private String name;
        private String loginName;
        private String role;


        public Long getId() {
            return id;
        }

        public UserInfo setId(Long id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public UserInfo setName(String name) {
            this.name = name;
            return this;
        }

        public String getLoginName() {
            return loginName;
        }

        public UserInfo setLoginName(String loginName) {
            this.loginName = loginName;
            return this;
        }

        public String getRole() {
            return role;
        }

        public UserInfo setRole(String role) {
            this.role = role;
            return this;
        }
    }

}
