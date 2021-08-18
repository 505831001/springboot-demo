package org.liuweiwei.utils;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liuweiwei
 * @since 2021-08-18
 */
@Component
@Log4j2
public class JwtTokenUtils implements Serializable {

    private static final String  dbUsername = "admin";
    private static final String     subject = dbUsername;
    private static final String  dbPassword = Base64Utils.encodeToString("123456".getBytes());
    private static final String      secret = dbPassword;
    private static final String         key = secret;

    private static final String               header = "response.setHeader('Authorization', token)";
    private static final String       tokenStartWith = "Bearer ";
    private static final String         base64Secret = "x1x2o3o4k6k7b890";
    private static final Long tokenValidityInSeconds = 14400000L;
    private static final String      AUTHORITIES_KEY = "auth";

    public String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claim(AUTHORITIES_KEY, claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + tokenValidityInSeconds))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        HashMap map = (HashMap) claims.get("auth");
        User principal = new User(map.get("user").toString(), map.get("password").toString(), authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            e.printStackTrace();
        }
        return false;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
