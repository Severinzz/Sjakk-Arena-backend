package no.ntnu.sjakkarena.restcontrollers;


import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;


import no.ntnu.sjakkarena.utils.KeyHelper;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Handles requests from players
 */
@RestController
@RequestMapping("/pushnotification")

public class PushNotificationRESTController {

    private static HashMap<Integer, Subscription> pushRegistrations = new HashMap<>();
    private static PushService pushService = new PushService();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getPublicKey() {
        String applicationServerKey = KeyHelper.getPublicKey();
        return new ResponseEntity<>(applicationServerKey, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Subscription> setPushSubscription(@RequestBody Subscription sub){
        int playerId = RESTSession.getUserId();
        if(!pushRegistrations.containsKey(playerId)){
            pushRegistrations.put(playerId, sub);
        }
        return new ResponseEntity<>(sub, HttpStatus.OK);
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePushSubscription(){
        int playerId = RESTSession.getUserId();
        String response;
        if(pushRegistrations.containsKey(playerId)){
            pushRegistrations.remove(playerId);
            response = "Successfully unsubscribed";
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = "Subscription not registered";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendPushNotification(int userId, String gameJsonString) {
        Security.addProvider(new BouncyCastleProvider());
        if(pushRegistrations.containsKey(userId)) {
            try {
                pushService.setPublicKey(KeyHelper.getPublicKey());
                pushService.setPrivateKey(KeyHelper.getPrivateKey());

                Subscription sub = pushRegistrations.get(userId);
                Notification notification = new Notification(sub, gameJsonString);
                pushService.send(notification);
            } catch (IOException | ExecutionException | GeneralSecurityException | JoseException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
