package com.prueba.springbootapp.services;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.prueba.springbootapp.models.Client;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY = "d32f42b93843e8874d4bd6efcbe92b6811c6ddab035c07d6b808784e98a63dd4";

    /* for clients users */
    public String generateToken(Client user) {
        String token = Jwts
            .builder()
            .subject(user.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60))
            .signWith(getSigningKey())
            .compact();
        return token;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public boolean isValid(String token, UserDetails user){
        String savedUsername = extractUsername(token);
        String claimingUsername = user.getUsername();
        boolean isTokenExpired = extractClaim(token, Claims::getExpiration).before(new Date());

        return (savedUsername.equals(claimingUsername) && !isTokenExpired);
    }


    private Claims extractAllClaims(String token) {
           return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
