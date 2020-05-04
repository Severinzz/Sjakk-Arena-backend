package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.HashedElement;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Generates hashes
 *
 * Adaption from https://www.baeldung.com/java-password-hashing
 */
public class HashGenerator {

    /**
     * Returns a random salt
     *
     * @return a random salt
     */
    public static byte[] getRandomSalt(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomSalt = new byte[15];
        secureRandom.nextBytes(randomSalt);
        return randomSalt;
    }

    /**
     * Hashes the provided string and returns a HashedElement object containing the hash and the salt
     *
     * @param s The string to hash
     * @return A HashedElement object containing the hash and the salt
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static HashedElement hash(String s) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = getRandomSalt();
        return hash(s, salt);
    }

    /**
     * Hashes the provided string using the provided salt and returns a HashedElement object containing
     * the hash and the salt
     *
     * @param s The string to hash
     * @param salt The salt to use in the hashing
     * @return A HashedElement object containing the hash and the salt
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static HashedElement hash(String s, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
         byte[] saltArray = Base64.getDecoder().decode(salt);
         return hash(s, saltArray);
    }

    /**
     * Hashes the provided string using the provided salt and returns a HashedElement object containing
     * the hash and the salt
     *
     * @param s The string to hash
     * @param salt The salt to use in the hashing
     * @return A HashedElement object containing the hash and the salt
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static HashedElement hash(String s, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec keySpec = new PBEKeySpec(s.toCharArray(), salt, 1000, 128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return new HashedElement(hash, salt);
    }
}
