package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.context.ApplicationEvent;
import java.util.List;
public class NewGamesEvent extends ApplicationEvent {

    public List<Game> games;

    public NewGamesEvent(Object source, List<Game> games) {
        super(source);
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }
}
