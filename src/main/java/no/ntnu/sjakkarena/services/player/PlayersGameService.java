package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.ResultUpdateRequest;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.services.EventService;
import no.ntnu.sjakkarena.utils.RESTSession;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayersGameService extends EventService {

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    private GameRepository gameRepository;


    public List<? extends Game> getInactiveGames(int playerId) {
        return gameWithPlayerNamesRepository.getInActiveGames(playerId);
    }

    public Game getActiveGame(int playerId) {
        return gameWithPlayerNamesRepository.getActiveGame(playerId);
    }

    public void suggestResult(ResultUpdateRequest resultUpdateRequest) {
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
        if (game.isValidResult()){
            throw new IllegalArgumentException("Game has already been validated");
        }
        gameRepository.makeGameValid(gameID);
    }
}
