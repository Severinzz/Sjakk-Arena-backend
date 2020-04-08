package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
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
        try {
            sendToSubscriber(
                    playerRemovedEvent.getPlayerId(), "/queue/player/removed",
                    playerRemovedEvent.getRemoveReason());
        } catch(NullPointerException e){
            printNotSubscribingErrorMessage("remove player", e);
        }
    }

    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent){
        for (Player player : tournamentStartedEvent.getPlayers()){
            informPlayerAboutTournamentState(player.getId(), true);
        }

    }

    @EventListener
    public void onResultAdded(ResultAddedEvent resultAddedEvent){
        sendPointsToPlayer(resultAddedEvent.getPlayer1());
        sendValidResultInformationToPlayer(resultAddedEvent.getPlayer1().getId());
        sendPointsToPlayer(resultAddedEvent.getPlayer2());
        sendValidResultInformationToPlayer(resultAddedEvent.getPlayer2().getId());

    }

    @EventListener
    public void onResultSuggested(ResultSuggestedEvent resultSuggestedEvent){
        sendResultInformationToPlayer(resultSuggestedEvent.getOpponentId(), resultSuggestedEvent.getResult(), resultSuggestedEvent.getGameId(),
                false, false);
    }

    @EventListener
    public void onResultInvalidated(InvalidResultEvent invalidResultEvent){
        Game game = invalidResultEvent.getGame();
        sendInvalidResultInformationToPlayer(game.getWhitePlayerId(), game);
        sendInvalidResultInformationToPlayer(game.getBlackPlayerId(), game);
    }

    private void sendValidResultInformationToPlayer(int playerId){
        sendResultInformationToPlayer(playerId, null, null, false, true);
    }

    private void sendInvalidResultInformationToPlayer(int playerId, Game game){
        sendResultInformationToPlayer(playerId, null, game.getGameId(),
                true, false);
    }

    private void sendResultInformationToPlayer(int playerId, Double result, Integer gameId, boolean opponentsDisagree,
                                               boolean validResult){
        try{
            sendToSubscriber(playerId, "/queue/player/result",
                    jsonCreator.createResponseToResultSubscriber(result,
                            gameId, opponentsDisagree, validResult));
        } catch(NullPointerException e){
            printNotSubscribingErrorMessage("player's result", e);
        }
    }

    public void sendPointsToPlayer(Player player){
        try{
            sendToSubscriber(player.getId(), "/queue/player/points",
                    jsonCreator.createResponseToPlayerPointsSubscriber(player.getPoints()));
        } catch(NullPointerException e){
            printNotSubscribingErrorMessage("player's points", e);
        }
    }

    public void informPlayerAboutTournamentState(int playerId, boolean active) {
        try{
            sendToSubscriber(playerId, "/queue/player/tournament-active",
                    jsonCreator.createResponseToTournamentStateSubscriber(active));
        } catch(NullPointerException e){
            printNotSubscribingErrorMessage("tournament status", e);
        }
    }

    private void sendGameToWhiteAndBlackPlayer(Game game){
        sendGame(game, game.getWhitePlayerId());
        sendGame(game, game.getBlackPlayerId());
    }

    public void sendGame(Game game, int playerId) {
        try {
            sendToSubscriber(playerId, "/queue/player/active-game",
                    jsonCreator.filterGameInformationAndReturnAsJson(game, playerId));
        }
        catch(NullPointerException e){
            printNotSubscribingErrorMessage("games to players", e);
        }
    }
}
