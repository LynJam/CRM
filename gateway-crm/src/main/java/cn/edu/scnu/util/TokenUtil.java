package cn.edu.scnu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TokenUtil {
    /**
     * token 过期时间
     */
    private static final long TOKEN_EXPIRE_TIME = TimeUnit.MILLISECONDS.convert(20, TimeUnit.MINUTES);
    /**
     * refresh_token 过期时间
     */
    public static final long REFRESH_TOKEN_EXPIRE_TIME = TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS);
    /**
     * token 签名秘钥
     */
    private static final String TOKEN_SECRET = "abc123";
    /**
     * refresh_token 签名秘钥
     */
    private static final String REFRESH_TOKEN_SECRET = "123abc";

    public static String generateToken(String userId) {
        return generateAnyToken(userId, TOKEN_EXPIRE_TIME, TOKEN_SECRET);
    }

    public static String getUserIdByToken(String token) {
        return getUserIdByAnyToken(token, TOKEN_SECRET);
    }

    public static String generateRefreshToken(String userId) {
        return generateAnyToken(userId, REFRESH_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_SECRET);
    }

    public static String getUserIdByRefreshToken(String token) {
        return getUserIdByAnyToken(token, REFRESH_TOKEN_SECRET);
    }

    private static String generateAnyToken(String userId, long expireTime, String tokenSecret) {
        Date now = new Date();
        Date expireData = new Date(now.getTime() + expireTime);
        String token = Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(now)
            .setExpiration(expireData)
            .signWith(SignatureAlgorithm.HS512, tokenSecret)
            .compact();
        return token;
    }

    private static String getUserIdByAnyToken(String token, String tokenSecret) {
        if (token == null) {
            return null;
        }
        String realToken = token;
        if (token.startsWith("Bearer ")) {
            realToken = token.substring(7);
        }
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(realToken)
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}

