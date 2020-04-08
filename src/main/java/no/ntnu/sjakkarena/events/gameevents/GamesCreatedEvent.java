package no.ntnu.sjakkarena.events.gameevents;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.context.ApplicationEvent;
import java.util.List;
public class GamesCreatedEvent extends ApplicationEvent {

    private List<? extends Game> activeGames;

    private int tournamentId;

    public GamesCreatedEvent(Object source, List<? extends Game> activeGames, int tournamentId) {
        super(source);
        this.activeGames = activeGames;
        this.tournamentId = tournamentId;
    }

    public List<? extends Game> getActiveGames() {
        return activeGames;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
