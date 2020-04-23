package no.ntnu.sjakkarena.restcontrollers;

import java.io.IOException;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.KeyHelper;

import no.ntnu.sjakkarena.data.PushRegistration;

import no.ntnu.sjakkarena.utils.RESTSession;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Handles requests from players
 */
@RestController
@RequestMapping("/pushnotification")

public class PushNotificationRESTController {

    private static HashMap<Integer, PushRegistration> pushRegistrations = new HashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getPublicKey(){
        String publicKey =  KeyHelper.publicKey();
        return new ResponseEntity<>(publicKey, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PushRegistration> setPushSubscription(@RequestBody PushRegistration registration){
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println(registration.getKeys().getAuth());
//        System.out.println(registration.getEndpoint());
//        System.out.println(registration.getExpirationTime());
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
//        System.out.println("------------------------------------------------------------------");
        pushRegistrations.put(59, registration);
        sendPostRequest(registration);
        return new ResponseEntity<>(registration, HttpStatus.OK);
    }

    // https://github.com/web-push-libs/webpush-java/wiki/Usage-Example

    public void sendPostRequest(PushRegistration registration){
        try {
//            Notification notification;
            PushService pushService = new PushService();
//            byte[] test = new byte[64];
            pushService.setPrivateKey(KeyHelper.getPrivateKey());
            pushService.setPublicKey(KeyHelper.getPublicKey());
//            String jwt = JWSHelper.createJWSWithPrivateKey(KeyHelper.getPublicKey(), KeyHelper.getPrivateKey(), registration);
//            notification = new Notification(
//                    registration.getEndpoint(),
//                    String.valueOf(KeyHelper.getPublicKey()),
//                    Base64.getDecoder().decode(registration.getKeys().getAuth()));
            Notification noti = new Notification(
                    registration,
                    "BPgGiVRn44VOLWeDzv6mQwSYb4f3cULYZSDpG0T2BnFvmkIJnVyU2Y_RM9pAG2jJ1J2TocKQoj8okLNHkHC9td4"
            );
            pushService.send(noti);
//            pushService.send
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (JoseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
