package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.User;
import org.springframework.security.core.Authentication;

public class WebSocketSession {

    /**
     * Returns the id of a user using WebSockets
     *
     * @param authentication An authentication
     * @return THe id of a user using WebSockets
     */
    public static int getUserId(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
