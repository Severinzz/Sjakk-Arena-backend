package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayersTournamentService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    public Tournament getTournament(int playerId) {
        try {
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            return tournamentRepository.getTournament(tournamentId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    public boolean isTournamentActive(int playerId){
        try {
            Player player = playerRepository.getPlayer(playerId);
            return tournamentRepository.isActive(player.getTournamentId());
        } catch (NotInDatabaseException e){
            throw new NotInDatabaseException(e);
        }
    }
}
