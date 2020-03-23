package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;
public class PlayerListChangeEvent extends ApplicationEvent {

    List<Player> players;

    List<Player> leaderBoard;

    private boolean tournamentHasStarted;

    private int tournamentId;

    public PlayerListChangeEvent(Object source, List<Player> players, List<Player> leaderBoard,
                                 boolean tournamentHasStarted, int tournamentId) {
        super(source);
        this.players = players;
        this.leaderBoard = leaderBoard;
        this.tournamentHasStarted = tournamentHasStarted;
        this.tournamentId = tournamentId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getLeaderBoard() {
        return leaderBoard;
    }

    public boolean hasTournamentStarted() {
        return tournamentHasStarted;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
