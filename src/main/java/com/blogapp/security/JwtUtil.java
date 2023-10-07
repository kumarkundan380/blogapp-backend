package com.blogapp.security;

import com.blogapp.exception.BlogAppException;
import com.blogapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtUtil {

    private final String secretKey;

    private final Long expirationTime;

    private final UserRepository userRepository;

    @Autowired
    public JwtUtil(@Value("${blog.app.jwt.secret}") String secretKey, @Value("${blog.app.jwt.expiration.time}") Long expirationTime, UserRepository userRepository) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
        this.userRepository = userRepository;
    }

    // Generate Token
    public String generateToken(String subject) {
        log.info("generateToken method invoking");
        return Jwts.builder()
                .setSubject(subject)
                .setIssuer("BlogApp")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime + TimeUnit.MILLISECONDS.toMillis(expirationTime)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Read Claims
    public Claims getClaims(String token) {
        log.info("getClaims method invoking");
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
            throw new BlogAppException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw new BlogAppException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw new BlogAppException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid");
            throw new BlogAppException("JWT token compact of handler are invalid");
        }
    }

    // Get Expire Date
    public Date getExpiryDate(String token) {
        return getClaims(token).getExpiration();
    }

    // Get UserName
    public String getUserNameFromJwtToken(String token) {
        return getClaims(token).getSubject();
    }

    // Validate Expiration Date
    public boolean isTokenExpire(String token) {
        return getExpiryDate(token).before(new Date(System.currentTimeMillis()));
    }

    // Validate UserName in token and database, Expiration Time
    public boolean validateToken(String token, String userName) {
        return (getUserNameFromJwtToken(token).equals(userRepository.getUserByUserName(userName).getUserName()) && !isTokenExpire(token));
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
