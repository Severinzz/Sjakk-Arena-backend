package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.ResultUpdateRequest;
import no.ntnu.sjakkarena.eventcreators.GameEventCreator;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayersGameService {

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameEventCreator gameEventCreator;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    @Autowired
    private GameCreator gameCreator;


    public List<? extends Game> getInactiveGames(int playerId) {
        return gameWithPlayerNamesRepository.getInActiveGames(playerId);
    }

    public Game getActiveGame(int playerId) {
        return gameWithPlayerNamesRepository.getActiveGame(playerId);
    }

    public void addResult(ResultUpdateRequest resultUpdateRequest) {
        if (!Validator.pointsIsValid(resultUpdateRequest.getResult())) {
            throw new IllegalArgumentException("Not a valid result");
        }
        try {
            Game game = gameRepository.getActiveGame(RESTSession.getUserId(), resultUpdateRequest.getOpponent());
            gameRepository.addResult(game.getGameId(), resultUpdateRequest.getResult());
            onSuggestedResult(resultUpdateRequest.getOpponent(), resultUpdateRequest.getResult(), game.getGameId());
        } catch (NotInDatabaseException e){
            throw e;
        }
    }

    public void setGameResultValid(int gameID) {
        Game game = gameRepository.getGame(gameID);
        if (game.getWhitePlayerId() != RESTSession.getUserId() && game.getBlackPlayerId() != RESTSession.getUserId()){
            throw new IllegalArgumentException("Requesting user is not playing the game with gameId " + gameID);
        }
        gameRepository.makeResultValid(gameID);
        gameRepository.deactivateGame(gameID);
        onResultValidated(gameID);
    }


    public void invalidateResult(int gameId) {
        gameRepository.makeResultInvalid(gameId);
        gameEventCreator.createAnPublishInvalidResultEvent(gameId);
    }

    private void onSuggestedResult(int opponentId, double result, int gameId) {
        gameEventCreator.createAndPublishResultSuggestedEvent(opponentId, result, gameId);
    }

    private void onResultValidated(int gameId) {
        Game game = gameRepository.getGame(gameId);
        int tournamentId = playerRepository.getPlayer(game.getWhitePlayerId()).getTournamentId();
        playerEventCreator.createAndPublishPlayerListChangeEvent(tournamentId);
        gameEventCreator.createAndPublishValidResultAddedEvent(gameId);
        gameCreator.createAndPublishNewGames(tournamentId, new AfterTournamentStartAdaptedMonrad());
    }
}
