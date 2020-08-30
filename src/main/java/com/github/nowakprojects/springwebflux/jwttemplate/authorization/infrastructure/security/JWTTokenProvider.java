package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTTokenProvider {

    private static final String USER_ID_IN_TOKEN = "userId";
    private final JwtTokenConfig jwtTokenConfig;

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(jwtTokenConfig.secret().getBytes())).parseClaimsJws(token).getBody();
    }

    String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    String getUserIdFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get(USER_ID_IN_TOKEN);
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_IN_TOKEN, user.getUserId().getRaw());
        return doGenerateToken(claims, user.getUsername().getRaw());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        long expirationTimeLong = Long.parseLong(jwtTokenConfig.expirationTime());

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(jwtTokenConfig.secret().getBytes()))
                .compact();
    }

    Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
