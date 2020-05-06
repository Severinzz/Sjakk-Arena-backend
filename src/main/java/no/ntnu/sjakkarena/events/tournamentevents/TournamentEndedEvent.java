package no.ntnu.sjakkarena.events.tournamentevents;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * An event where a tournament has ended. This event is classified as a tournament event
 */
public class TournamentEndedEvent extends ApplicationEvent {


    private int tournamentId;

    private List<Player> players;

    /**
     * Constructs a TournamentEndedEvent
     *
     * @param source The object on which the Event initially occurred.
     *             (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param tournamentId The id of the ended tournament
     * @param players List of players who played in the ended tournament
     */
    public TournamentEndedEvent(Object source, int tournamentId, List<Player> players) {
        super(source);
        this.tournamentId = tournamentId;
        this.players = players;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
