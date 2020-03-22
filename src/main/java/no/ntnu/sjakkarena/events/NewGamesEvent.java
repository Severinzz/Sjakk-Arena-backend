package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.context.ApplicationEvent;
import java.util.List;
public class NewGamesEvent extends ApplicationEvent {

    private List<? extends Game> games;

    private int tournamentId;

    public NewGamesEvent(Object source, List<? extends Game> games, int tournamentId) {
        super(source);
        this.games = games;
        this.tournamentId = tournamentId;
    }

    public List<? extends Game> getGames() {
        return games;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
