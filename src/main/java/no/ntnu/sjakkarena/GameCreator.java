package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
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
        createNewGames(tournamentId, adaptedMonrad);
        gameEventCreator.createAndPublishGamesCreatedEvent(tournamentId);
    }

    private void createNewGames(int tournamentId, AdaptedMonrad adaptedMonrad){
        List<Player> playersInTournamentNotPlaying = playerRepository.getPlayersInTournamentNotPlaying(tournamentId);
        List<Integer> availableTables = tournamentRepository.getAvailableTables(tournamentId);
        gameRepository.addGames(adaptedMonrad.getNewGames(playersInTournamentNotPlaying, availableTables));
    }
}
