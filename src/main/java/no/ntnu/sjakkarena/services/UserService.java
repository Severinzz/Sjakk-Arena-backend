package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.PlayerListChangeEvent;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO change name
@Service
public abstract class UserService {
    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    protected TournamentRepository tournamentRepository;

    protected void onPlayerListChange(int tournamentId) {
        // TODO make an own service class containing methods for setting up events
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getPlayersInTournamentSortedByPoints(tournamentId);
        applicationEventPublisher.publishEvent(new PlayerListChangeEvent(this, players, leaderBoard,
                tournamentHasStarted, tournamentId));
    }
}
