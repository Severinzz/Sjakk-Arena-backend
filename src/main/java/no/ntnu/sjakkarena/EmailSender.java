package no.ntnu.sjakkarena;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class EmailSender {

    private JavaMailSender emailSender;

    public EmailSender(){
        // Code from https://www.baeldung.com/spring-email
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(YOUR_MAIL_ADRESS);
        mailSender.setPassword(YOUR_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        emailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text){
        // Code from https://www.baeldung.com/spring-email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
