package no.ntnu.sjakkarena.events.gameevents;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * An event where a game has been created. This event is classified as a game event.
 */
public class GamesCreatedEvent extends ApplicationEvent {

    private List<? extends Game> createdGames;

    private List<? extends Game> activeGames;

    private int tournamentId;

    /**
     * Constructs a GamesCreatedEvent
     *
     * @param source       The object on which the Event initially occurred.
     *                     (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param activeGames  The games that is active after the creation of the new games
     * @param createdGames The games that have been created
     * @param tournamentId The id of the tournament the games are associated with
     */
    public GamesCreatedEvent(Object source, List<? extends  Game> activeGames, List<? extends Game> createdGames,
                             int tournamentId) {
        super(source);
        this.activeGames = activeGames;
        this.createdGames = createdGames;
        this.tournamentId = tournamentId;
    }

    public List<? extends Game> getCreatedGames() {
        return createdGames;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public List<? extends Game> getActiveGames() {
        return activeGames;
    }
}
