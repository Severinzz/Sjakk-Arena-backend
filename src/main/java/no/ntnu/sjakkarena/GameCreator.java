package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.eventcreators.GameEventCreator;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameCreator {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private GameEventCreator gameEventCreator;


    public void createAndPublishNewGames(int tournamentId, AdaptedMonrad adaptedMonrad){
        List<Integer> gameIds = createNewGames(tournamentId, adaptedMonrad);
        gameEventCreator.createAndPublishGamesCreatedEvent(tournamentId, gameIds);
    }

    private List<Integer> createNewGames(int tournamentId, AdaptedMonrad adaptedMonrad){
        List<Player> playersInTournamentNotPlaying = playerRepository.getPlayersInTournamentNotPlaying(tournamentId);
        List<Integer> availableTables = tournamentRepository.getAvailableTables(tournamentId);
        List<Game> createdGames = adaptedMonrad.getNewGames(playersInTournamentNotPlaying, availableTables);
        List<Integer> gameIds = gameRepository.addGames(createdGames);
        return gameIds;
    }
}
