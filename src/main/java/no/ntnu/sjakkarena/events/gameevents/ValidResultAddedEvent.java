package no.ntnu.sjakkarena.events.gameevents;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

/**
 * An event where a valid result is added to a game. This event is classified as a game event
 */
public class ValidResultAddedEvent extends ApplicationEvent {

    private Player whitePlayer;
    private Player blackPlayer;

    /**
     * Constructs a ValidResultAddedEvent
     *
     * @param source      The object on which the Event initially occurred.
     *                    (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param whitePlayer The player playing with white chessmen in the game the result is associated with
     * @param blackPlayer The player playing with black chessmen in the game the result is associated with
     */
    public ValidResultAddedEvent(Object source, Player whitePlayer, Player blackPlayer) {
        super(source);
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
}
