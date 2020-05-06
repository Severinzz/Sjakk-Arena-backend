package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.utils.KeyHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class SjakkarenaApplication {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        KeyHelper.writeKeyToFile();
        SpringApplication.run(SjakkarenaApplication.class, args);
    }
}
