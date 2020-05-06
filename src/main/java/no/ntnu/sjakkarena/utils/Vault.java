package no.ntnu.sjakkarena.utils;

import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

/**
 * Communicates with the vault where secrets are stored
 */
public class Vault {

    private static VaultTemplate vaultTemplate;


    /**
     * Initialises the Vault
     *
     * @param token The token used to open the vault
     */
    public static void init(String token) {
        VaultEndpoint vaultEndpoint = new VaultEndpoint();
        vaultEndpoint.setScheme("http");
        vaultTemplate = new VaultTemplate(vaultEndpoint, new TokenAuthentication(token));
    }

    /**
     * Writes a secret to the specified destination in the vault
     *
     * @param destination The destination where the secret will be stored
     * @param secret      The secret to store
     */
    public static void write(String destination, Object secret) {
        vaultTemplate.write("kv/sjakkarena/" + destination, secret);
    }

    /**
     * Returns a secret stored at the specified destination
     *
     * @param destination The destination where the secret is stored
     * @param c           The class of the secret to be returned
     * @param <T>         The type of the secret to be returned
     * @return a secret stored at the specified destination
     */
    public static <T> VaultResponseSupport<T> read(String destination, Class<T> c) {
        VaultResponseSupport<T> response = vaultTemplate.read("kv/sjakkarena/" + destination, c);
        return response;
    }
}
