package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.NewGamesEvent;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public void addResult(int opponentId, double whitePlayerPoints) {
        // TODO change sql query to find game regardless of who is white or black --> remove the first if statement
        Game game = gameRepository.getActiveGame(RESTSession.getUserId(), opponentId); // Has requesting user white pieces?
        if (game == null) {
            game = gameRepository.getActiveGame(opponentId, RESTSession.getUserId()); // Has requesting user black pieces?
        }
        if (game == null) {
            throw new NotInDatabaseException("Player has no active games");
        }
        gameRepository.addResult(game.getGameId(), whitePlayerPoints);
        onResultAdded();
    }

    private void onResultAdded(){
        int tournamentId = playerRepository.getPlayer(RESTSession.getUserId()).getTournamentId();
        List<Game> newGames = requestNewGames(tournamentId);
        NewGamesEvent newGamesEvent = new NewGamesEvent(this, newGames, tournamentId);
        // TODO add games to database
        applicationEventPublisher.publishEvent(newGamesEvent);
    }

    private List<Game> requestNewGames(int tournamentId){
        List<Player> inActivePlayers = playerRepository.getPlayersWhoIsCurrentlyNotPlaying(tournamentId);
        List<Integer> availableTables = tournamentRepository.getAvailableTables(tournamentId);
        return AfterTournamentStartAdaptedMonrad.getNewGames(inActivePlayers, availableTables);
    }
}
