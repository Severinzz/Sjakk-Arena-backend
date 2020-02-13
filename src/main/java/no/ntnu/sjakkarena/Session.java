package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.data.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Session {

    public static int getUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
