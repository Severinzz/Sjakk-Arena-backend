package no.ntnu.sjakkarena;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;

/**
 * Adapted from https://stackoverflow.com/questions/32801391/using-javamail-gmail-refusing-authentication-due-to-application-being-less-secu
 */
@Component
public class EmailSender {

    private Transport transport;

    private Session session;

    private String username = "USERNAME";

    private String password = "PASSWORD";

    /**
     * Constructs an email sender
     */
    public EmailSender() {
        try {
            Properties props = getProperties();
            session = getSession(props);
            transport = session.getTransport("smtp");
            connect();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns relevant properties
     *
     * @return relevant properties
     */
    private Properties getProperties(){
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return props;
    }

    /**
     * Returns a session object with username and password + the specified properties
     *
     * @param props properties
     * @return a session object with username and password + the specified properties
     */
    private Session getSession(Properties props){
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Connects to the smtp server
     *
     * @throws MessagingException
     */
    private void connect() throws MessagingException {
        transport.connect("smtp.gmail.com", username, password);
    }

    /**
     * Sends email
     *
     * @param to      The recipient of the email
     * @param subject The subject of the email
     * @param text    The text content of the email
     */
    public void sendEmail(String to, String subject, String text) {
        try {
            sendEmail(to, subject, text, 1);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends email
     *
     * @param to      The recipient of the email
     * @param subject The subject of the email
     * @param text    The text content of the email
     * @param numberOfTriesLeft The number of tries left to send the email
     */
    private void sendEmail(String to, String subject, String text, int numberOfTriesLeft) throws MessagingException {
        try {
            MimeMessage message = createMessage(new InternetAddress(to), new InternetAddress(username), subject, text);
            transport.sendMessage(message, message.getAllRecipients());
        } catch(MessagingException | IllegalStateException e){
            e.printStackTrace();
            tryToSendAgain(to, subject, text, numberOfTriesLeft);
        }
    }


    /**
     * Creates a message to be sent to the specified address
     *
     * @param toAddress The address to send the email to
     * @param fromAddress The address the email is sent from
     * @param subject The subject of the email
     * @param text The text content email
     * @return
     * @throws MessagingException
     */
    private MimeMessage createMessage(Address toAddress, Address fromAddress, String subject, String text) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(fromAddress);
        message.setRecipients(TO, new Address[]{toAddress});
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    /**
     * Tries to send a message again if number of tries less is greater than zero
     *
     * @param to The address to send the email to
     * @param subject The subject of the email
     * @param text The text content email
     * @oaram numberOfTriesLeft
     * @return
     * @throws MessagingException
     */
    private void tryToSendAgain(String to, String subject, String text, int numberOfTriesLeft) throws MessagingException {
        if (numberOfTriesLeft > 0){
            connect();
            sendEmail(to, subject, text, numberOfTriesLeft - 1);
        }
    }
}
