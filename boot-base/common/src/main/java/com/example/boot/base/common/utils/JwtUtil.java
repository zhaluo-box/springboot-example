package com.example.boot.base.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * Created  on 2022/6/27 14:14:11
 *
 * @author wmz
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtil {

    public static final String JWT_KEY_ID = "id";

    public static final String JWT_KEY_USER_NAME = "username";

    /**
     * 私钥加密token
     *
     * @param claims        载荷中的数据
     * @param privateKey    私钥
     * @param expireMinutes 过期时间，单位秒
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claims, PrivateKey privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                   .addClaims(claims)
                   .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                   .signWith(SignatureAlgorithm.RS256, privateKey)
                   .compact();
    }

    /**
     * 私钥加密token
     *
     * @param claims        载荷中的数据
     * @param privateKey    私钥字节数组
     * @param expireMinutes 过期时间，单位秒
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claims, byte[] privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                   .addClaims(claims)
                   .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                   .signWith(SignatureAlgorithm.RS256, RsaUtil.getPrivateKey(privateKey))
                   .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return 载荷
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @return 载荷
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtil.getPublicKey(publicKey)).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 载荷信息
     * @throws Exception
     */
    public static Map<String, Object> getInfoFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        return claimsJws.getBody();
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static Map<String, Object> getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        return claimsJws.getBody();
    }
}
