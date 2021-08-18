package org.liuweiwei.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Liuweiwei
 * @since 2021-08-18
 */
@Component
@Log4j2
public class JwtUtils {

    private static final String dbUsername = "admin";
    private static final String    subject = dbUsername;
    private static final String dbPassword = Base64Utils.encodeToString("123456".getBytes());
    private static final String     secret = dbPassword;
    private static final String        key = secret;
    private static final long       expire = 1800L;
    private static final Date   expireDate = new Date(System.currentTimeMillis() + expire * 1000);

    /**
     * 生成jwt token
     * @param userInfoMap
     * @return
     */
    public static String generateToken(Map<String, Object> userInfoMap) {
        if (Objects.isNull(userInfoMap)) {
            userInfoMap = new HashMap<>();
        }
        /**
         * 设置头部信息。
         * 装入自定义的用户信息。
         * token过期时间。
         * 加密的secret。
         * 紧凑的URL安全JWT字符串。
         */
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(userInfoMap)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }


    /**
     * 校验token并解析token
     * @param token
     * @return Claims：它继承了Map,而且里面存放了生成token时放入的用户信息
     */
    public static Claims verifyAndGetClaimsByToken(String token) {
        try {
            /**
             * 如果过期或者是被篡改，则会抛异常。
             * 注意点：只有在生成token设置了过期时间(setExpiration(expireDate))才会校验是否过期，可以参考源码io.jsonwebtoken.impl.DefaultJwtParser的299行。
             * 拓展：利用不设置过期时间就不校验token是否过期的这一特性，我们不设置Expiration，而采用自定义的字段来存放过期时间放在Claims（可以简单的理解为map）中。
             * 通过token获取到Claims后自己写代码校验是否过期。通过这思路，可以去实现对过期token的手动刷新。
             */
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception ex) {
            log.debug("verify token error: {}", ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }
}
