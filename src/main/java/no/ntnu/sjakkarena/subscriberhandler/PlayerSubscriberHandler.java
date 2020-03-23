package no.ntnu.sjakkarena.subscriberhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.events.GamesCreatedEvent;
import org.springframework.context.event.EventListener;

public class PlayerSubscriberHandler extends SubscriberHandler {

    /**
     *
     * @param gamesCreatedEvent
     */
    @EventListener
    public void onGamesCreated(GamesCreatedEvent gamesCreatedEvent) {
        for (Game game : gamesCreatedEvent.getActiveGames()){
            sendGameToWhiteAndBlackPlayer(game);
        }
    }

    private void sendGameToWhiteAndBlackPlayer(Game game){
        sendGame(game, game.getWhitePlayerId());
        sendGame(game, game.getBlackPlayerId());
    }

    private void sendGame(Game game, int playerId) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            sendToSubscriber(playerId, "/queue/player/games", gson.toJson(game));
        }
        catch(NullPointerException e){
            printNotSubscribingErrorMessage("games to players", e);
        }
    }
}
