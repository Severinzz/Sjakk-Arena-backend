package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.utils.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class SubscriberHandler {

    @Autowired
    protected SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Prints an error message when the user to be notified doesn't subscribe to a service
     * @param serviceDescription A description of the service
     * @param exception
     */
    protected void printNotSubscribingErrorMessage(String serviceDescription, Exception exception){
        System.out.println("User is probably not subscribing to the service providing " + serviceDescription
                + "\n");
        exception.printStackTrace();
    }

    protected void sendToSubscriber(int userId, String destination, String payload){
        User user = UserStorage.getUser(userId);
        simpMessagingTemplate.convertAndSendToUser(user.toString(), destination, payload);
    }
}
