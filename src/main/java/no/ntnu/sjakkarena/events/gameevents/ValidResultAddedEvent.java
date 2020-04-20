package no.ntnu.sjakkarena.events.gameevents;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

public class ValidResultAddedEvent extends ApplicationEvent {

    private Player player1;
    private Player player2;

    private int tournamentId;

    public ValidResultAddedEvent(Object source, Player player1, Player player2, int tournamentId) {
        super(source);
        this.player1 = player1;
        this.player2 = player2;
        this.tournamentId = tournamentId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
