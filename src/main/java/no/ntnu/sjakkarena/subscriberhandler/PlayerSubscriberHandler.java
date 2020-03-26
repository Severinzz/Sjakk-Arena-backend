package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.PlayerRemovedEvent;
import no.ntnu.sjakkarena.events.TournamentStartedEvent;
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

    @EventListener
    public void onPlayerRemoved(PlayerRemovedEvent playerRemovedEvent){
        sendToSubscriber(
                playerRemovedEvent.getPlayerId(), "/queue/player/removed",
                playerRemovedEvent.getRemoveReason());
    }
  
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent){
        for (Player player : tournamentStartedEvent.getPlayers()){
            informPlayerThatTournamentHasStarted(player);
        }

    }


    private void informPlayerThatTournamentHasStarted(Player player) {
        try{
            sendToSubscriber(player.getId(), "/queue/player/tournament-status",
                    jsonCreator.createResponseToTournamentStateRequester(true));
        } catch(NullPointerException e){
            printNotSubscribingErrorMessage("tournament status", e);
        }
    }

    private void sendGameToWhiteAndBlackPlayer(Game game){
        sendGame(game, game.getWhitePlayerId());
        sendGame(game, game.getBlackPlayerId());
    }

    private void sendGame(Game game, int playerId) {
        try {
            sendToSubscriber(playerId, "/queue/player/active-game", jsonCreator.writeValueAsString(game));
        }
        catch(NullPointerException e){
            printNotSubscribingErrorMessage("games to players", e);
        }
    }
}
