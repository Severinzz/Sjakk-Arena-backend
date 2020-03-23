package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.events.GamesCreatedEvent;
import org.springframework.context.event.EventListener;

public class PlayerSubscriberHandler extends SubscriberHandler {

    private JSONCreator jsonCreator = new JSONCreator();

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
            sendToSubscriber(playerId, "/queue/player/games", jsonCreator.writeValueAsString(game));
        }
        catch(NullPointerException e){
            printNotSubscribingErrorMessage("games to players", e);
        }
    }
}
