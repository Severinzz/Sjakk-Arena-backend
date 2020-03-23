package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.TournamentStartedEvent;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    private PlayerRepository playerRepository;


    private JSONCreator jsonCreator = new JSONCreator();

    public void startTournament() {
        try {
            tournamentRepository.setActive(RESTSession.getUserId());
            onTournamentStart();
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getTournament() {
        try {
            int tournamentId = RESTSession.getUserId();
            Tournament tournament = tournamentRepository.getTournament(tournamentId);
            return jsonCreator.writeValueAsString(tournament);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }

    public void deletePlayer(int playerId) {
        try {
            playerRepository.deletePlayer(playerId);
            // TODO change to event handling
            tournamentSubscriberHandler.sendPlayerList(RESTSession.getUserId());
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getGamesWithPlayerNames() {
        int tournamentId = RESTSession.getUserId();
        Collection<GameWithPlayerNames> games = gameWithPlayerNamesRepository.getGamesWithPlayerNames(tournamentId);
        return jsonCreator.writeValueAsString(games);
    }

    private void onTournamentStart() {
        int tournamentId = RESTSession.getUserId();
        applicationEventPublisher.publishEvent(new TournamentStartedEvent(this, tournamentId));
    }



}
