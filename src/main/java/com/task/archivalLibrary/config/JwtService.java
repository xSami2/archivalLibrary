package com.task.archivalLibrary.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {


    private static final String SECRET_KEY = "awOqxGd2xPpXK0WM8PfeDAhcdCKEHeDy";

    public String extractUsername(String jwt) {
        return extractClaims(jwt, Claims::getSubject);
    }

    private Claims extractClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(jwt)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaims(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))  // 24 Hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isJwtValid(String jwt , UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username != null && username.equals(userDetails.getUsername()) && isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaims(jwt, Claims::getExpiration);
    }


}
