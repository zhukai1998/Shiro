package cn.zhukai.shiro.infrastructure.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.istack.internal.NotNull;

import java.util.Date;

/**
 * <p>
 * NOTE:此加密方式非非对称加密，可自行改造
 * NOTE:双token保证活跃用户
 * https://cloud.tencent.com/developer/news/837117
 * 为用户签发两个token：accessToken、refreshToken
 * accessToken: 时长较短
 * refreshToken：时长较长
 * 当 access_token 过期后，判断 refreshToken 是否过期，若没过期，则通过 refreshToken 刷新获取新的 access_token，如果都过期，就需要重新登录。
 *
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/15 15:50
 **/


public class JwtUtil {
    // token 的载荷中存放的信息：用户的ID
    private final static String ID_TOKEN_CLAIM = "id";
    // token 过期时间 ，默认为 1 天
    private final static long EXPIRE_TIME_TOKEN = 1 * 24 * 60 * 60 * 1000L;


    /**
     * 返回token
     *
     * @param salt
     * @param id
     * @param expireTimeMills
     * @return
     */
    public static String sign(String salt, @NotNull String id, Long expireTimeMills) {
        if (expireTimeMills == null) {
            expireTimeMills = EXPIRE_TIME_TOKEN;
        }
        Date date = new Date(System.currentTimeMillis() + expireTimeMills);
        // 对称加密
        Algorithm algorithm = Algorithm.HMAC256(salt);
        // 非对称加密
//        Algorithm algorithm = Algorithm.RSA256("private RSA Key");
        return JWT.create()
                .withClaim(ID_TOKEN_CLAIM, id)
                // TODO
                .withExpiresAt(date)
                .sign(algorithm);
    }


    /**
     * 验证 token 是否正确，并返回 ID
     *
     * @param token
     * @param salt  若只是获取ID，salt可为null
     * @return
     */
    public static String verify(String token, String salt) {
        try {
            // 获得 token 中的信息无需 salt 解密也能获得
            DecodedJWT jwt = JWT.decode(token);
            String id = jwt.getClaim(ID_TOKEN_CLAIM).asString();
            if (salt != null) {
                // 根据盐值生成 JWT 校验器
                Algorithm algorithm = Algorithm.HMAC256(salt);
//                Algorithm algorithm = Algorithm.RSA256("public RSA Key");
                JWTVerifier verifier = JWT.require(algorithm)
                        .withClaim(ID_TOKEN_CLAIM, id)
                        .build();
                // 校验 token
                verifier.verify(token);
            }
            return id;
        } catch (Exception e) {
            return null;
        }

    }
}
