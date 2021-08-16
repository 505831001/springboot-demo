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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
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
 * @author Liuweiwei
 * @since 2021-08-06
 */
@Component
@Log4j2
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    protected JwtTokenUtils jwtTokenUtils;
    protected JwtTokenUtil  jwtTokenUtil;

    private String base64EncodedSecretKey = "secret";

    private String dbUsername = "admin";
    private String dbPassword = new BCryptPasswordEncoder().encode("123456");
    private String dbRoles    = "ADMIN,GUEST";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //第一步从请求头中获取token
        String requestHeader = request.getHeader("Authentication");
        String generateToken = request.getHeader("Authorization");

        Map<String, Object> claims2 = new HashMap<>(10);
        claims2.put("auth", dbRoles);
        claims2.put("password", dbPassword);

        generateToken = Jwts.builder()
                .setClaims(claims2)
                .setSubject(dbUsername)
                .setIssuedAt(DefaultClock.INSTANCE.now())
                .setExpiration(new Date(DefaultClock.INSTANCE.now().getTime() + 7200000L))
                .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey)
                .compact();
        log.info("[step 01] Using jjwt generate a token:" + generateToken);
        requestHeader = Jwts.builder()
                .claim("auth", claims2)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(DefaultClock.INSTANCE.now().getTime() + 7200000L))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey)
                .compact();
        log.info("[step 01] Using http generate a token:" + requestHeader);

        String tokenStartsWith = "Bearer" + " " + generateToken;
        String      startsWith = "Bearer" + " ";
        //第二步从Token中获取用户信息
        if (StringUtils.hasText(tokenStartsWith) && tokenStartsWith.startsWith(startsWith)) {
            String authToken = tokenStartsWith.substring(startsWith.length());
            if (StringUtils.hasText(authToken)) {
                Jws<Claims>  jws = Jwts.parser().setSigningKey("secret").parseClaimsJws(authToken);
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
                Jws<Claims> jws = Jwts.parser().setSigningKey("secret").parseClaimsJws(authToken);
                Claims   claims = jws.getBody();
                String  subject = claims.getSubject();
                Date expiration = claims.getExpiration();
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
