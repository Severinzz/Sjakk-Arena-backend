package no.ntnu.sjakkarena.events.playerevents;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Player;

import java.util.List;
public class NewPlayerAddedEvent extends PlayerListChangeEvent {

    private AdaptedMonrad adaptedMonrad;

    public NewPlayerAddedEvent(Object source, List<Player> players, List<Player> leaderBoard,
                               int tournamentId, boolean tournamentHasStarted) {
        super(source, players, leaderBoard, tournamentHasStarted, tournamentId);
        this.adaptedMonrad = new AfterTournamentStartAdaptedMonrad();
    }

    public AdaptedMonrad getAdaptedMonrad() {
        return adaptedMonrad;
    }
}
