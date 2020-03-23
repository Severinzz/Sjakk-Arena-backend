package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class RESTSession {

    public static int getUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
