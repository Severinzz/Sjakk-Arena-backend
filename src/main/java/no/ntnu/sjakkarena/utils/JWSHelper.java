package no.ntnu.sjakkarena.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import no.ntnu.sjakkarena.data.PushRegistration;
import no.ntnu.sjakkarena.data.SubscriptionKeys;
import no.ntnu.sjakkarena.data.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JWSHelper {

    /**
     * Creates a JWS with the given subject and id
     *
     * @param subject
     * @param id
     * @return JWS as a String
     */
    public static String createJWS(String subject, String id) {
        Key secretKey = KeyHelper.getKey();
        String jws = Jwts.builder().setSubject(subject).setId(id).signWith(secretKey).compact();
        return jws;
    }

    /**
     * Validates a JWS. Code from https://stackoverflow.com/questions/41975045/how-to-design-a-good-jwt-authentication-filter
     *
     * @param jws The jws to be validated
     * @return True if the jws is valid, otherwise false
     */
    public static boolean validateToken(String jws) {
        try {
            Jwts.parserBuilder().setSigningKey(KeyHelper.getKey()).build().parseClaimsJws(jws);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * Get an authentication with the authorities as described by the JWS
     *
     * @param jws The JWS where the information needed for authentication is stored
     * @return An authentication.
     */
    public static Authentication getAuthentication(String jws) {
        Claims claims = Jwts.parserBuilder().setSigningKey(KeyHelper.getKey())
                .build().parseClaimsJws(jws).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(claims.getSubject()).stream()
                .map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

        User principal = new User(Integer.parseInt(claims.getId()));
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * Creates a JWS with the given subject and id
     * https://metamug.com/article/security/sign-verify-digital-signature-ecdsa-java.html
     */
    public static String createJWSWithPrivateKey(PublicKey publicKey, PrivateKey privateKey, PushRegistration registration) throws NoSuchAlgorithmException, java.security.SignatureException, InvalidKeyException {
        // Mobile & distri start
        Date now = new Date();
        Date expiration = Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant());
       //end

       // https://metamug.com/article/security/sign-verify-digital-signature-ecdsa-java.html
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);
        //end

        return Jwts.builder().setAudience(registration.getEndpoint()).setExpiration(expiration).signWith(privateKey).compact();
    }

}
