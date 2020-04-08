package no.ntnu.sjakkarena.events.playerevents;

import org.springframework.context.ApplicationEvent;

public class PlayerRemovedEvent extends ApplicationEvent {

    private int playerId;
    private String removeReason;

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
