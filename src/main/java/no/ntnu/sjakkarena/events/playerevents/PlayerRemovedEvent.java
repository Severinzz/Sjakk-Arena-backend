package no.ntnu.sjakkarena.events.playerevents;

import org.springframework.context.ApplicationEvent;

/**
 * An event where a player is removed. This event is classified as en player event
 */
public class PlayerRemovedEvent extends ApplicationEvent {

    private int playerId;
    private String removeReason;
    private Boolean wasKicked;

    /**
     * Constructs a PlayerRemovedEvent
     *
     * @param source The object on which the Event initially occurred.
     *            (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param playerId The id of the removed player
     * @param removeReason The reason the player was removed
     * @param wasKicked Value to tell if the player was kicked.
     */
    public PlayerRemovedEvent(Object source, int playerId, String removeReason, Boolean wasKicked) {
        super(source);
        this.playerId = playerId;
        this.removeReason = removeReason;
        this.wasKicked = wasKicked;
    }

    public int getPlayerId(){
        return this.playerId;
    }

    public String getRemoveReason(){
        return this.removeReason;
    }

    public boolean getWasKicked() { return this.wasKicked; }
}
