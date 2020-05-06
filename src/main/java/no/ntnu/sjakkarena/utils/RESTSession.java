package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class RESTSession {

    /**
     * Returns the requesting user's id
     *
     * @return the requesting user's id
     */
    public static int getUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    /**
     * Returns the requesting user's authorities
     *
     * @return the requesting user's authorities
     */
    public static List<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    }
}
