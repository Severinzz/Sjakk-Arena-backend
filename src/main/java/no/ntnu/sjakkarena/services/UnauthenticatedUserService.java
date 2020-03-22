package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.NewPlayerAddedEvent;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UnauthenticatedUserService {

    // TODO use events instead
    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public String addNewPlayer(Player player) {
        try {
            player.setIcon(PlayerIcons.getRandomFontAwesomeIcon());
            int userId = playerRepository.addNewPlayer(player);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("jwt", JWSHelper.createJWS("PLAYER", "" + userId));
            onNewPlayerAdd(player.getTournamentId());
            return jsonResponse.toString();
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    private void onNewPlayerAdd(int tournamentId) {
        tournamentSubscriberHandler.sendPlayerList(tournamentId);
        if (tournamentRepository.getTournament(tournamentId).isActive()) {
            NewPlayerAddedEvent newPlayerAddedEvent = new NewPlayerAddedEvent(this, tournamentId);
            applicationEventPublisher.publishEvent(newPlayerAddedEvent);
        }
    }
}
