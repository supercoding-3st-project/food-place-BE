package com.github.foodplacebe.config.security;

import com.github.foodplacebe.repository.users.BlacklistedToken;
import com.github.foodplacebe.repository.users.BlacklistedTokenJpa;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenConfig {
    private final UserDetailsService userDetailsService;
    private final BlacklistedTokenJpa blacklistedTokenJpa;

    @Value("${jwtpassword.source}")
    private String keySource;
    private String key;
    @PostConstruct
    public void setUp(){
        key = Base64.getEncoder()
                .encodeToString(keySource.getBytes());
    }


    public boolean validateToken(String token){
        try{//.ExpiredJwtException 토큰검증실패시 캐치문으로 넘어감.
            Claims claims = Jwts.parser()
                    .setSigningKey(key).parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }
    public boolean isTokenBlacklisted(String jwtToken) {
        if(blacklistedTokenJpa.existsById(jwtToken)){
            log.warn("로그아웃 처리된 토큰");
            return true;
        }else {
            return false;
        }
    }

    public BlacklistedToken addBlacklist(String token) {
        Date expiration = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration();
        LocalDateTime localDateTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return blacklistedTokenJpa.save(new BlacklistedToken(token,
                localDateTime));
    }

    public Authentication getAuthentication(String jwtToken) {
        String email = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String createToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(email)
                .setExpiration(new Date(now.getTime()+1000L * 60 *60))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

}
