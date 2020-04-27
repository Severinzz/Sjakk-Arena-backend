package no.ntnu.sjakkarena.events.gameevents;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.context.ApplicationEvent;

/**
 * An event where a result is declared invalid. This event is classified as a game event
 */
public class InvalidResultEvent extends ApplicationEvent {

    private Game game;
    private int tournamentId;

    /**
     * Constructs an InvalidResultEvent
     *
     * @param source       The object on which the Event initially occurred.
     *                     (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param game         The game the invalidated result is associated with
     * @param tournamentId The id of the tournament the game is associated with
     */
    public InvalidResultEvent(Object source, Game game, int tournamentId) {
        super(source);
        this.game = game;
        this.tournamentId = tournamentId;
    }

    public Game getGame() {
        return game;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
