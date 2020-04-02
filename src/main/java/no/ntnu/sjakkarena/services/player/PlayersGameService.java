package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayersGameService {

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    public List<? extends Game> getInactiveGames(int playerId) {
        return gameWithPlayerNamesRepository.getInActiveGames(playerId);
    }

    public Game getActiveGame(int playerId) {
        return gameWithPlayerNamesRepository.getActiveGame(playerId);
    }
}
