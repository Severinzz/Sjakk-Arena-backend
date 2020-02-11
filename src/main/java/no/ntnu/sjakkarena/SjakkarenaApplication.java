package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.utils.Security;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SjakkarenaApplication {

	public static void main(String[] args) {
		Security.writeKeyToFile();
		SpringApplication.run(SjakkarenaApplication.class, args);
	}
}
