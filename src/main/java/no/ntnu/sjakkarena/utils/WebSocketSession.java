package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.User;
import org.springframework.security.core.Authentication;

public class WebSocketSession {
    public static int getUserId(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
