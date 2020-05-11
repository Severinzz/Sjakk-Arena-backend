package no.ntnu.sjakkarena.events.tournamentevents;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TournamentPausedEvent extends ApplicationEvent {

    private int tournamentId;
    private List<Player> players;

    /**
     * Constructs a TournamentPausedTournamentEvent
     *
     * @param source       The object on which the Event initially occurred.
     *                     (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param tournamentId The id of the tournament which is getting paused
     */
    public TournamentPausedEvent(Object source, int tournamentId, List<Player> players) {
        super(source);
        this.tournamentId = tournamentId;
        this.players = players;
    }

    public int getTournamentId() {
        return this.tournamentId;
    }

    public List<Player> getPlayers() { return this.players; }
}
