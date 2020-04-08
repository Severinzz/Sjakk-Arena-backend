package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.context.ApplicationEvent;

public class InvalidResultEvent extends ApplicationEvent {

    private Game game;
    private int tournamentId;

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
