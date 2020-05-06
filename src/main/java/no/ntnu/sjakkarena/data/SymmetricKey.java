package no.ntnu.sjakkarena.data;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

/**
 * Represents a symmetric key.
 */
public class SymmetricKey implements SecretKey {

    private String algorithm;
    private String format;
    private byte[] encoded;


    /**
     * Constructs a symmetric key
     */
    public SymmetricKey(){
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.algorithm = secretKey.getAlgorithm();
        this.format = secretKey.getFormat();
        this.encoded = secretKey.getEncoded();
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public byte[] getEncoded() {
        return encoded;
    }
}
