package com.example.springboot_cy_marketplace.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider{
    @Value("${jwt.app.JWT_SECRET}")
    private String JWT_SECRET;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    //  Tạo Token từ username của người dùng với thời gian thời gian tồn tại của accessToken đó / Generate Token from user's username with lifetime of that accessToken
    public String generateTokenFormUsername(String username,long timeValid){
        Date expiryDate = generateExpirationDate(timeValid);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiryDate )
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }
    public String getEmailFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Set thời gian tồn tại cho token / set lifetime for token
    public Date generateExpirationDate(long timeValid) {
        return new Date(System.currentTimeMillis() + timeValid * 1000);
    }
    // Get Claims từ Token / Get Claims form Token
    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }
    // Kiểm tra token đã hết hạn hay chưa / Check if the token has expired or not
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    // Lấy thông tin email của người dùng đã đăng nhập thông qua token / Get email information of logged in users via token
    public String getEmailFromJwtToken(String token) {
        Claims claims = getClaimsFromJwtToken(token);
        if (claims != null && isTokenExpired(claims)) {
            return claims.getSubject();
        }
        return null;
    }
    // Xác thực token / Validate Token
    public boolean validateJwtToken(String authToken) throws Exception {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
//            throw new Exception("Invalid JWT token: {} ");
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
//            throw new Exception("JWT token is expired: {}");
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}