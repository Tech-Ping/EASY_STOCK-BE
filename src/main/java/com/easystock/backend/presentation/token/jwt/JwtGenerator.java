package com.easystock.backend.presentation.token.jwt;

import com.easystock.backend.aspect.exception.MemberNotFoundException;
import com.easystock.backend.aspect.exception.TokenInvalidTypeException;
import com.easystock.backend.aspect.exception.UnauthorizedTokenException;
import com.easystock.backend.presentation.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

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
                .setExpiration(new Date(System.currentTimeMillis()+ jwtProperties.getExpiration().getAccess()))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(Long memberId){
        return Jwts.builder()
                .setHeader(createTokenHeader(TokenType.REFRESH_TOKEN))
                .setClaims((Map.of(MEMBER_ID_KEY_NAME, memberId)))
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

            if (memberId == null) throw new MemberNotFoundException();
            if (tokenType == null) throw new TokenInvalidTypeException();

            return new Token(
                    Long.valueOf(memberId.toString()),
                    TokenType.valueOf(tokenType.toString())
            );

        }
        catch (JwtException e){
            throw new UnauthorizedTokenException();
        }
    }

    public void validateAccessToken(String accessToken){
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
            jwtParser.parseClaimsJws(accessToken);
        } catch(JwtException e){
            throw new TokenInvalidTypeException();
        }
    }

    private Map<String, Object> createTokenHeader(TokenType tokenType){
        return Map.of(
                "alg", "HS256",
                "typ","JWT",
                "regDate", System.currentTimeMillis(),
                TOKEN_TYPE_KEY_NAME, tokenType.getName());
    }
}
