package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.utils.KeyHelper;
import no.ntnu.sjakkarena.utils.Vault;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Console;
import java.security.Security;
import java.util.Scanner;

@SpringBootApplication
public class SjakkarenaApplication {

    public static void main(String[] args) {
        Vault.init(getVaultToken());
        Security.addProvider(new BouncyCastleProvider());
        KeyHelper.createAndStoreKey();
        SpringApplication.run(SjakkarenaApplication.class, args);
    }

    /**
     * Get vault token from the application user
     *
     * @return The vault token
     */
    private static String getVaultToken() {
        System.out.println("Please enter your vault token: ");
        String token = null;
        if (System.console() != null) {
            Console console = System.console();
            token = new String(console.readPassword());
        } else {
            Scanner scanner = new Scanner(System.in);
            token = scanner.nextLine();
        }
        return token;
    }
}
