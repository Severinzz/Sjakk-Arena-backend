package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

public class ResultAddedEvent extends ApplicationEvent {

    Player player1;
    Player player2;

    public ResultAddedEvent(Object source, Player player1, Player player2) {
        super(source);
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}
