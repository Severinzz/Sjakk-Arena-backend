package no.ntnu.sjakkarena.events.tournamentevents;

import org.springframework.context.ApplicationEvent;

/**
 * An event where it is time to end a tournament. This event is classified as a tournament event
 */
public class TimeToEndTournamentEvent extends ApplicationEvent {

    private int tournamentId;

    /**
     * Constructs a TimeToEndTournamentEvent
     *
     * @param source The object on which the Event initially occurred.
     *       (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param tournamentId The id of the tournament which it is time to end
     */
    public TimeToEndTournamentEvent(Object source, int tournamentId) {
        super(source);
        this.tournamentId = tournamentId;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
