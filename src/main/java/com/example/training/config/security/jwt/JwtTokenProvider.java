package com.example.training.config.security.jwt;

import com.example.training.service.implementation.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;


/* Tạo Token khi đăng nhập thành công. */
@Component
@Service
public class JwtTokenProvider implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.setting.jwtSecret}")
    private String JWT_Secret;

    private final long JWT_EXPIRATION = 604800000L;

    public String generateJwtToken(UserDetailsImpl userDetailsImp){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject((userDetailsImp.getUsername()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_Secret)
                .compact();
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_Secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parser().setSigningKey(JWT_Secret).parseClaimsJws(authToken);
            return true;
        }catch (SignatureException e){
            logger.error("Invalid JWT signature.");
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired.");
        }catch (UnsupportedJwtException e){
            logger.error("JWT token is unsupported.");
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
