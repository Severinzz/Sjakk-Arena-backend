package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.EncryptionKey;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.vault.support.VaultResponseSupport;

import java.security.Key;


/**
 * Handles public and private keys
 */
public class KeyHelper {

    private static EncryptionKey key = new EncryptionKey();

    /**
     * Returns a secret key
     *
     * @return a secret key
     */
    public static Key getKey() {
        VaultResponseSupport<EncryptionKey> response = Vault.read("symmetric", EncryptionKey.class);
        return response.getData();
    }

    /**
     * Creates and stores a secret key
     */
    public static void createAndStoreKey() {
        try {
            getKey();
        } catch (HttpMessageConversionException | NullPointerException e){
            Vault.write("symmetric", key);
        }
    }

    /**
     * Returns public key
     * See this for easy keys in right format https://web-push-codelab.glitch.me/
     *
     * @return Public key
     */
    public static String getPublicKey() {
        return "";
    }

    /**
     * Returns private key
     * See this for easy keys in right format https://web-push-codelab.glitch.me/
     *
     * @return Private key
     */
    public static String getPrivateKey() {
        return "";
    }

}
