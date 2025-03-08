package com.example.websocket.chatting.common.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    private SecretKey SECRET_KEY;

    @Value("${jwt.secretkey}")
    private String secretKeyPlain;

    //jwt 요효시간
    private final long EXPIRATION_TIME = 1000 * 60 * 60; //1시간

    private SecretKey generateSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());

        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public SecretKey getSecretKey() {
        if (SECRET_KEY == null) SECRET_KEY = generateSecretKey();
        return SECRET_KEY;
    }

    //jwt 생성
    public String generateJwt(String nickName) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(nickName) //사용자 닉네임
                .setIssuedAt(new Date()) //발행 시간
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    //해당 서버에서 만든 jwt 인지 검증
    public boolean validateJwt(String jwt) {
        try {
            jwt = jwt.replace("Bearer ", "");
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //jwt 에서 nickName 구하기
    public String extractNickNameAtJwt(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Jwts.parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    //jwt 유효기간 확인
    public boolean isJwtExpired(String jwt) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
