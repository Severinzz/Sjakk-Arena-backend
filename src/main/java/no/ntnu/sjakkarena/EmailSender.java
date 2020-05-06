package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.data.EmailAccount;
import no.ntnu.sjakkarena.utils.Vault;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Sends emails
 */
@Component
public class EmailSender {

    private JavaMailSender emailSender;

    /**
     * Constructs an email sender
     * Code from https://www.baeldung.com/spring-email
     */
    public EmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        EmailAccount emailAccount = Vault.read("email", EmailAccount.class).getData();
        mailSender.setUsername(emailAccount.getEmail());
        mailSender.setPassword(emailAccount.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        emailSender = mailSender;
    }

    /**
     * Sends email
     * Code from https://www.baeldung.com/spring-email
     *
     * @param to      The recipient of the email
     * @param subject The subject of the email
     * @param text    The text content of the email
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
