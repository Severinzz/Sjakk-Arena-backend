package no.ntnu.sjakkarena.restcontrollers;


import nl.martijndwars.webpush.Subscription;

import no.ntnu.sjakkarena.controllers.PushNotificationController;
import no.ntnu.sjakkarena.utils.KeyHelper;
import no.ntnu.sjakkarena.utils.RESTSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Handles push notification requests from players
 */
@RestController
@RequestMapping("/pushnotification")

public class PushNotificationRESTController {

    @Autowired
    PushNotificationController pushNotificationController;

    /**
     * Returns the public key from the public-private key pair.
     * @return Public key from the public-private key pair.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getPublicKey() {
        String applicationServerKey = KeyHelper.getPublicKey();
        return new ResponseEntity<>(applicationServerKey, HttpStatus.OK);
    }

    /**
     * Saves the subscription object received from the user.
     * @param sub Subscription object from the user's browser.
     * @return The given subscription object.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Subscription> setPushSubscription(@RequestBody Subscription sub){
        int playerId = RESTSession.getUserId();
        pushNotificationController.addPushNotification(playerId, sub);
        return new ResponseEntity<>(sub, HttpStatus.OK);
    }

    /**
     * Unsubscribe the user form the push notifications.
     * @return Returns a string describing if the unsubscription was successful or not.
     */
    @RequestMapping(value = "/unsubscribe", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePushSubscription(){
        int playerId = RESTSession.getUserId();
        String response;
        if(pushNotificationController.removePushNotification(playerId)){
            response = "Successfully unsubscribed";
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            response = "Subscription not registered";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
