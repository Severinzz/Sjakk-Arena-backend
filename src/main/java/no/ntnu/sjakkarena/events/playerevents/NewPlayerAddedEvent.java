package no.ntnu.sjakkarena.events.playerevents;

import no.ntnu.sjakkarena.data.Player;

import java.util.List;

/**
 * An event where a player is added to a tournament. This event extends the PlayerListChangeEvent and
 * is classified as a player event
 */
public class NewPlayerAddedEvent extends PlayerListChangeEvent {


    /**
     * Constructs an NewPlayerAddedEvent
     *
     * @param source               The object on which the Event initially occurred.
     *                             (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param players              List of players in the tournament to which the player is added
     * @param leaderBoard          The leader board in the tournament to which the player is added
     * @param tournamentId         The id of the tournament to which the player is added
     * @param tournamentHasStarted Whether the tournament to which the player is added has started
     */
    public NewPlayerAddedEvent(Object source, List<Player> players, List<Player> leaderBoard,
                               int tournamentId, boolean tournamentHasStarted) {
        super(source, players, leaderBoard, tournamentHasStarted, tournamentId);
    }

}
