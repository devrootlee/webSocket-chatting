package com.example.websocket.chatting.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private final String SECRET_KEY = "sadfdsafsdfdsfsd";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; //1시간

    //jwt 생성
    public String generateJwt(String id) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(id) //사용자 아이디
                .setIssuedAt(new Date()) //발행 시간
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //해당 서버에서 만든 jwt 인지 검증
    public boolean validateJwt(String jwt) {
        try {
            jwt = jwt.replace("Bearer ", "");
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //jwt 에서 userId 구하기
    public String extractUserIdAtJwt(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    //jwt 유효기간 확인
    public boolean isJwtExpired(String jwt) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
