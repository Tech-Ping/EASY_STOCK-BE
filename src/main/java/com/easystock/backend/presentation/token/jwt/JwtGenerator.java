package com.easystock.backend.presentation.token.jwt;

import com.easystock.backend.aspect.exception.TokenInvalidTypeException;
import com.easystock.backend.aspect.exception.UnauthorizedTokenException;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import com.easystock.backend.presentation.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static com.easystock.backend.util.Constants.MEMBER_ID_KEY_NAME;
import static com.easystock.backend.util.Constants.TOKEN_TYPE_KEY_NAME;

@Component
public class JwtGenerator implements TokenGenerator {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    public JwtGenerator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    @Override
    public String generateAccessToken(Long memberId){
        return Jwts.builder()
                .setHeader(createTokenHeader(TokenType.ACCESS_TOKEN))
                .setClaims(Map.of(MEMBER_ID_KEY_NAME, memberId))
                .setSubject(String.valueOf(memberId))
                .setExpiration(new Date(System.currentTimeMillis()+ jwtProperties.getExpiration().getAccess()))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(Long memberId){
        return Jwts.builder()
                .setHeader(createTokenHeader(TokenType.REFRESH_TOKEN))
                .setClaims((Map.of(MEMBER_ID_KEY_NAME, memberId)))
                .setSubject(String.valueOf(memberId))
                .setExpiration(new Date(System.currentTimeMillis()+ jwtProperties.getExpiration().getRefresh()))
                .signWith(secretKey)
                .compact();
    }

    public JwtParser getJwtParser(){
        return Jwts.parserBuilder()
                .setSigningKey((secretKey))
                .build();
    }

    @Override
    public Token extractTokenData(String token){
        try {
            Claims claims = getJwtParser().parseClaimsJws(token).getBody();
            Object memberId = claims.get(MEMBER_ID_KEY_NAME);
            Object tokenType = claims.get(TOKEN_TYPE_KEY_NAME);

            if (memberId == null) throw new AuthException(String.valueOf(ErrorStatus.MEMBER_NOT_FOUND));
            if (tokenType == null) throw new TokenInvalidTypeException();

            return new Token(
                    Long.valueOf(memberId.toString()),
                    TokenType.valueOf(tokenType.toString())
            );

        }
        catch (JwtException e){
            throw new UnauthorizedTokenException();
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateAccessToken(String accessToken){
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
            jwtParser.parseClaimsJws(accessToken);
            return true;
        } catch(JwtException e){
            throw new TokenInvalidTypeException();
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
            jwtParser.parseClaimsJws(refreshToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        Token token = extractTokenData(refreshToken);
        return generateAccessToken(token.memberId());
    }

    private Map<String, Object> createTokenHeader(TokenType tokenType){
        return Map.of(
                "alg", "HS256",
                "typ","JWT",
                "regDate", System.currentTimeMillis(),
                TOKEN_TYPE_KEY_NAME, tokenType.getName());
    }
}
