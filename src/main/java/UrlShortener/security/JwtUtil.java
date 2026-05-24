package UrlShortener.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.Date;

public class JwtUtil {

    private static final Key SECRET_KEY =
            Keys.secretKeyFor(
                    SignatureAlgorithm.HS256
            );

    // Generate Token
    public static String generateToken(
            String username
    ){

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(
                        new Date()
                )

                .setExpiration(
                        new Date(
                            System.currentTimeMillis()
                            + 1000 * 60 * 60 * 24
                        )
                )

                .signWith(SECRET_KEY)

                .compact();
    }

    // Extract Username
    public static String extractUsername(
            String token
    ){

        Claims claims =
                Jwts.parserBuilder()

                    .setSigningKey(
                        SECRET_KEY
                    )

                    .build()

                    .parseClaimsJws(token)

                    .getBody();

        return claims.getSubject();
    }

    // Validate Token
    public static boolean validateToken(
            String token
    ){

        try{

            Jwts.parserBuilder()

                    .setSigningKey(
                        SECRET_KEY
                    )

                    .build()

                    .parseClaimsJws(token);

            return true;

        }catch(Exception e){

            return false;
        }
    }
}