package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This class enforces access control to some destinations (URIs)
 */
@Component
public class AccessControlInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TournamentRepository tournamentRepository = new TournamentRepository();

    @Autowired
    private PlayerRepository playerRepository;


    /**
     * Enforces access control to some destinations (URIs)
     *
     * @param request Http request
     * @param response Http response
     * @param handler
     * @return True if access to the destination is granted. Otherwise false.
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        List<String> urisWithLimitedAccess = getURIsWithLimitedAccess();
        boolean uriFound = false;
        int i = 0;
        boolean grantAccess = true;

        while (!uriFound && i < urisWithLimitedAccess.size()){
            if (request.getRequestURI().startsWith(urisWithLimitedAccess.get(i))){
                uriFound = true;
                grantAccess = isAccessAllowed();
            }
            i++;
        }

        if (!grantAccess) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tournament is either inactive or user is not registered");
        }
        return grantAccess;
    }

    /**
     * Returns true if access can be given to the requesting user
     *
     * @return true if access can be given to the requesting user
     */
    private boolean isAccessAllowed(){
        int userId = RESTSession.getUserId();
        List<? extends GrantedAuthority> authorities = RESTSession.getAuthorities();
        String authority = authorities.get(0).getAuthority();

        boolean accessAllowed = false;
        if (authority.equals("TOURNAMENT")){
            accessAllowed = isTournamentActive(userId);
        } else if (authority.equals("PLAYER")) {
            accessAllowed = isPlayersTournamentActive(userId);
        }
        return accessAllowed;
    }

    /**
     * Returns true if tournament is active
     *
     * @param tournamentId The id of a tournament
     * @return true if tournament is active
     */
    private boolean isTournamentActive(int tournamentId){
        try {
            return tournamentRepository.getTournament(tournamentId).isActive();
        } catch (NotInDatabaseException e){
            return false;
        }
    }

    /**
     * Returns true if the tournament the player is enrolled in is active
     *
     * @param playerId The id of a player
     * @return true if the tournament the player is enrolled in is active
     */
    private boolean isPlayersTournamentActive(int playerId){
        try {
            Player player = playerRepository.getPlayer(playerId);
            return tournamentRepository.getTournament(player.getTournamentId()).isActive();
        } catch(NotInDatabaseException e){
            return false;
        }
    }

    /**
     * Returns a list of URIs which is subject to access control
     *
     * @return list of URIs which is subject to access control
     */
    private List<String> getURIsWithLimitedAccess(){
        List<String> uris = new ArrayList<>();
        uris.add("/player/inactivate");
        uris.add("/player/games");
        uris.add("/tournament/end");
        uris.add("/tournament/pause");
        uris.add("/tournament/games");
        uris.add("/tournament/player/inactivate");
        return uris;
    }
}
