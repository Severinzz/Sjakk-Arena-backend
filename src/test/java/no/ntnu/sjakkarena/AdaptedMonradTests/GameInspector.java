package no.ntnu.sjakkarena.AdaptedMonradTests;

import java.util.List;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;

public class GameInspector {

    private List<Game> games;

    public GameInspector(List<Game> games) {
        this.games = games;
    }

    public boolean isPlayingAgainstEachOther(Player player1, Player player2){
        boolean isPlayingAgainstEachOther = false;
        for (Game game : games){
            if ((game.getWhitePlayerId() == player1.getId() && game.getWhitePlayerId() == player2.getId()) ||
            (game.getWhitePlayerId() == player2.getId() && game.getBlackPlayerId() == player1.getId())) {
                isPlayingAgainstEachOther = true;
            }
        }
        return isPlayingAgainstEachOther;
    }
}
