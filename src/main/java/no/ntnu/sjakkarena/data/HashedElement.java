package no.ntnu.sjakkarena.data;

import java.util.Base64;

/**
 * Represents something that is hashed
 */
public class HashedElement {

    private byte[] salt;

    private byte[] hash;

    /**
     * Constructs a HashedElement with a the hash of the element and the salt used to hash the element.
     *
     * @param hash The hash of the element
     * @param salt The salt used to hash the element
     */
    public HashedElement(byte[] hash, byte[] salt) {
        this.salt = salt;
        this.hash = hash;
    }

    public String getSaltAsString(){
        return Base64.getEncoder().encodeToString(salt);
    }

    public String getHashAsString(){
        return Base64.getEncoder().encodeToString(hash);
    }
}
