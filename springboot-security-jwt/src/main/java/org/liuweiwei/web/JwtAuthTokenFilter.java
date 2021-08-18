package org.liuweiwei.web;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.utils.JwtTokenUtil;
import org.liuweiwei.utils.JwtTokenUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Liuweiwei
 * @since 2021-08-06
 */
@Component
@Log4j2
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    protected JwtTokenUtils jwtTokenUtils;
    protected JwtTokenUtil  jwtTokenUtil;

    private static final Long  EXPIRE_TIME = 12 * 60 * 60 * 1000L;
    private static final String dbUserId   = "13412345678";
    private static final String dbUsername = "admin";
    private static final String    subject = dbUsername;
    private static final String dbPassword = Base64Utils.encodeToString("123456".getBytes());
    private static final String     secret = dbPassword;
    private static final String        key = secret;
    private static final String dbRoles    = "ADMIN,GUEST";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //第一步从请求头中获取token
        String requestHeader = request.getHeader("Authentication");
        String generateToken = request.getHeader("Authorization");
        String httpUserToken = request.getHeader("userToken");

        Map<String, Object> claims2 = new HashMap<>(10);
        claims2.put("auth", dbRoles);
        claims2.put("password", dbPassword);

        generateToken = Jwts.builder()
                .setClaims(claims2)
                .setSubject(dbUsername)
                .setIssuedAt(DefaultClock.INSTANCE.now())
                .setExpiration(new Date(DefaultClock.INSTANCE.now().getTime() + 7200000L))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        //log.info("Using jjwt 生成签证: {}", generateToken);
        requestHeader = Jwts.builder()
                .claim("auth", claims2)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(DefaultClock.INSTANCE.now().getTime() + 7200000L))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        //log.info("Using http 生成签证: {}", requestHeader);
        httpUserToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims2)
                .setExpiration(new Date(System.currentTimeMillis() + 1800L * 1000))
                .signWith(SignatureAlgorithm.HS512, "zxyTestSecret")
                .compact();
        //log.info("Using user 生成签证: {}", httpUserToken);

        String tokenStartsWith = "Bearer" + " " + generateToken;
        String      startsWith = "Bearer" + " ";
        //第二步从Token中获取用户信息
        if (StringUtils.hasText(tokenStartsWith) && tokenStartsWith.startsWith(startsWith)) {
            String authToken = tokenStartsWith.substring(startsWith.length());
            if (StringUtils.hasText(authToken)) {
                //JWT签名与本地计算的签名不匹配。无法断言JWT有效性，不应信任JWT有效性。
                String    secret = dbPassword;
                Jws<Claims>  jws = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
                Claims    claims = jws.getBody();
                Date  expiration = claims.getExpiration();
                boolean flag = expiration.before(DefaultClock.INSTANCE.now());
                //第三步验证Token是否有效
                if (flag == false) {
                    //第四步将用户信息添加到全局SecurityContextHolder
                    String   principal = claims.getSubject();
                    String credentials = claims.get("password").toString();
                    String[]     auths = claims.get("auth").toString().split(",");
                    Collection<? extends GrantedAuthority> authorities = Arrays.stream(auths).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        }

        tokenStartsWith = "Bearer" + " " + generateToken;
             startsWith = "Bearer" + " ";
        //第二步从Token中获取用户信息
        if (StringUtils.hasText(tokenStartsWith) && tokenStartsWith.startsWith(startsWith)) {
            String authToken = tokenStartsWith.substring(7);
            if (StringUtils.hasText(authToken)) {
                //JWT签名与本地计算的签名不匹配。无法断言JWT有效性，不应信任JWT有效性。
                String    secret = dbPassword;
                Jws<Claims> jws  = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
                Claims   claims  = jws.getBody();
                String  subject  = claims.getSubject();
                Date expiration  = claims.getExpiration();
                boolean flag = expiration.before(DefaultClock.INSTANCE.now());
                //第三步验证Token是否有效
                if (flag == false) {
                    //第四步将用户信息添加到全局SecurityContextHolder
                    String   principal = subject;
                    String credentials = claims.get("password").toString();
                    String       auths = claims.get("auth").toString();
                    Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(auths);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
