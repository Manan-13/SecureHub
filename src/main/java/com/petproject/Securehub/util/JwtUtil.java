package com.petproject.Securehub.util;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JwtUtil {
    private static final SecretKey SECRET_KEY;
    private static final long EXPIRATION = 15*60*1000; // 15 minutes

    private static final SecretKey REFRESH_SECRET;
    private static final long REFRESH_EXPIRATION = 86400000; // 1 day

    static {
        String secretString = "ThisIsAReallyStrongAndLongSecretKeyForJWTAuthentication1234567890";
        SECRET_KEY = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        REFRESH_SECRET = Keys.hmacShaKeyFor(secretString.replaceAll("0", "2").getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(String username,  Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validateToken(String token) {
        JwtParser jwtParser= Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public static Set<String> getRolesFromToken(String token) {
        JwtParser jwtParser= Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return new HashSet<>((List<String>) claims.get("roles"));
    }

    public static String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

}