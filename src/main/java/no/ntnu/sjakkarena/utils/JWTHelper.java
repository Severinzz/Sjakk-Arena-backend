package no.ntnu.sjakkarena.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class JWTHelper {

    public static String createJWT(String subject){
        Key privateKey = Security.getKey();
        String jws = Jwts.builder().setSubject(subject).signWith(privateKey).compact();
        return jws;
    }

    public static void readJWT(String jwt){
        Key publicKey = Security.getKey();
        Jws<Claims> jws = Jwts.parserBuilder()  // (1)
                .setSigningKey(publicKey)         // (2)
                .build()                    // (3)
                .parseClaimsJws(jwt);
    }
}
