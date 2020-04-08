package no.ntnu.sjakkarena.events.playerevents;

import no.ntnu.sjakkarena.data.Player;

import java.util.List;
public class NewPlayerAddedEvent extends PlayerListChangeEvent {


    public NewPlayerAddedEvent(Object source, List<Player> players, List<Player> leaderBoard,
                               int tournamentId, boolean tournamentHasStarted) {
        super(source, players, leaderBoard, tournamentHasStarted, tournamentId);
    }

}
