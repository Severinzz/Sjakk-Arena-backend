package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.utils.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    protected SimpMessagingTemplate simpMessagingTemplate;

    public void sendToSubscriber(int userId, String destination, String payload){
        try {
            User user = UserStorage.getUser(userId);
            simpMessagingTemplate.convertAndSendToUser(user.toString(), destination, payload);
        } catch (NullPointerException e) {
            throw new NotSubscribingException("User with user id, " + userId + ", is not subscribing to destination, " +
                    destination, e);
        }
    }
}
