package no.ntnu.sjakkarena.events.playerevents;

import org.springframework.context.ApplicationEvent;

/**
 * An event where a player is removed. This event is classified as en player event
 */
public class PlayerRemovedEvent extends ApplicationEvent {

    private int playerId;
    private String removeReason;

    /**
     * Constructs a PlayerRemovedEvent
     *
     * @param source The object on which the Event initially occurred.
     *            (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param playerId The id of the removed player
     * @param removeReason The reason the player was removed
     */
    public PlayerRemovedEvent(Object source, int playerId, String removeReason) {
        super(source);
        this.playerId = playerId;
        this.removeReason = removeReason;
    }

    public int getPlayerId(){
        return this.playerId;
    }

    public String getRemoveReason(){
        return this.removeReason;
    }
}
