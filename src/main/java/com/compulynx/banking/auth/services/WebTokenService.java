package com.compulynx.banking.auth.services;

import com.compulynx.banking.auth.models.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class WebTokenService {
    public static final String SECRET = "6B5273396D5336756D6D2F6865747A31354835794B56796349354B522F4736725367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public TokenResponse generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private TokenResponse createToken(Map<String, Object> claims, String userName) {
        var token = new TokenResponse();
        token.setAccessToken(Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact());
        token.setExpiresIn(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        token.setSubject(userName);
        token.setIssuedAt(new Date(System.currentTimeMillis()));
        return token;
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        try{
            return extractClaim(token, Claims::getSubject);
        }
        catch (Exception e){
            log.error("Token Validation Failed! {}", e.getLocalizedMessage());
        }
        return null;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if(username != null) {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
        else {
            return false;
        }
    }
}

