package com.example.bidly.global.util;

import com.example.bidly.domain.member.role.MemberRole;
import com.example.bidly.global.exception.ServerException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static com.example.bidly.global.exception.ErrorCode.INVALID_TOKEN;

@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.access.token}")
    private Long accessTokenTime;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(Long userId, String email, MemberRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .expiration(new Date(date.getTime() + accessTokenTime))
                .issuedAt(date)
                .signWith(key)
                .compact();
    }

    public String substringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw new ServerException(INVALID_TOKEN);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}