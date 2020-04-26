package no.ntnu.sjakkarena.events.playerevents;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * An event where a change in a tournament's list of players has occurred. This event is classified as a player event
 */
public class PlayerListChangeEvent extends ApplicationEvent {

    List<Player> players;

    List<Player> leaderBoard;

    private boolean tournamentHasStarted;

    private int tournamentId;

    /**
     * Constructs an PlayerListChangeEvent
     *
     * @param source The object on which the Event initially occurred.
     *       (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param players List of players in the tournament to which the player is added
     * @param leaderBoard The leader board in the tournament to which the player is added
     * @param tournamentHasStarted Whether the tournament to which the player is added has started
     * @param tournamentId The id of the tournament to which the player is added
     */
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
