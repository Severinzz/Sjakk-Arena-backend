package no.ntnu.sjakkarena.events.tournamentevents;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

/**
 * An event where a tournament has started. This class is event is classified as a tournament event
 */
import java.util.List;

public class TournamentStartedEvent extends ApplicationEvent {

    private int tournamentId;

    private List<Player> players;

    /**
     * Constructs a TournamentStartedEvent
     *
     * @param source       The object on which the Event initially occurred.
     *                     (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param tournamentId The id of the started tournament
     * @param players      A list of players playing in the started tournament
     */
    public TournamentStartedEvent(Object source, int tournamentId, List<Player> players) {
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
