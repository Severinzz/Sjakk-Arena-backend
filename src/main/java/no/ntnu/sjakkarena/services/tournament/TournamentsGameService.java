package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.eventcreators.GameEventCreator;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TournamentsGameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    private GameCreator gameCreator;

    @Autowired
    private GameEventCreator gameEventCreator;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    public List<? extends Game> getGamesWithInvalidResultToBeSentToTournamentHost(int tournamentId) {
        return gameWithPlayerNamesRepository.getGamesWithInvalidResult(tournamentId);
    }

    public void changeGameResult(int tournamentId, int gameId, double whitePlayerPoints) {
        if(!Validator.pointsIsValid(whitePlayerPoints)) {
            throw new IllegalArgumentException("Score: " + whitePlayerPoints + " is not valid");
        }
        try {
            gameRepository.addResult(gameId, whitePlayerPoints);
            gameRepository.makeResultValid(gameId);
            gameRepository.deactivateGame(gameId);
            onValidResultAdd(tournamentId, gameId);
        } catch (TroubleUpdatingDBException e){
            throw new TroubleUpdatingDBException(e);
        }
    }

    public Collection<? extends Game> getGames(int tournamentId) {
        return gameWithPlayerNamesRepository.getGamesWithPlayerNames(tournamentId);
    }
    public Collection<? extends Game> getActiveGames(int tournamentId) {
        return gameWithPlayerNamesRepository.getActiveGames(tournamentId);
    }

    private void onValidResultAdd(int tournamentId, int gameId){
        gameCreator.createAndPublishNewGames(tournamentId, new AfterTournamentStartAdaptedMonrad());
        gameEventCreator.createAndPublishValidResultAddedEvent(gameId);
        playerEventCreator.createAndPublishPlayerListChangeEvent(tournamentId);
    }
}
