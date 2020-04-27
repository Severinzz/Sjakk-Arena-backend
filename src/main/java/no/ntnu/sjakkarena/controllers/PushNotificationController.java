package no.ntnu.sjakkarena.controllers;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.utils.KeyHelper;

import org.jose4j.lang.JoseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class PushNotificationController {

    private static HashMap<Integer, Subscription> pushRegistrations = new HashMap<>();
    private static PushService pushService = new PushService();

    @Autowired
    private JSONCreator jsonCreator;


    /**
     * Listen for event where a new game was created.
     * Sends push notification for each player involved in the game.
     * @param gamesCreatedEvent Event when game is created
     */
    @EventListener
    public void onGamesCreated(GamesCreatedEvent gamesCreatedEvent) {
        for (Game game : gamesCreatedEvent.getActiveGames()){
            sendNotificationToWhiteAndBlackPlayer(game);
        }
    }

    /**
     * Listen for event where a tournament has ended.
     * Unsubscribe all players in the given tournament from push notifications
     * @param tournamentEndedEvent Event where tournament ended.
     */
    @EventListener
    public void onTournamentEnd(TournamentEndedEvent tournamentEndedEvent){
        unsubscribePlayers(tournamentEndedEvent.getPlayers());
    }

    /**
     * Adds the push subscription object to the HashMap.
     * @param playerId      Id of the subscribed player. Key in the HashMap
     * @param sub           Subscription object of the subscribed player. Value in the HashMap
     */
    public void addPushNotification(int playerId, Subscription sub){
            pushRegistrations.put(playerId, sub);
    }

    /**
     * Removes Removes the subscription of the given player.
     * @param       playerId Id of the player that wants to unsubscribe.
     * @return      Boolean value to tell if the subscription was removed or not
     */
    public boolean removePushNotification(int playerId) {
        boolean removed = false;
        if (pushRegistrations.containsKey(playerId)) {
            pushRegistrations.remove(playerId);
            removed = true;
        }
        return removed;
    }

    /**
     *  Sends push notification to the specific user.
     * @param userId            Id of the user you want to send notification to
     * @param gameJsonString    JSON string that contains whom the player is vs and the colour of the player given playerId.
     */
    private void sendPushNotification(int userId, Game game) {
        String gameAsJsonString = jsonCreator.filterGameInformationAndReturnAsJson(game, userId);
        if(pushRegistrations.containsKey(userId)) {
            try {
                pushService.setPublicKey(KeyHelper.getPublicKey());
                pushService.setPrivateKey(KeyHelper.getPrivateKey());

                Subscription sub = pushRegistrations.get(userId);
                Notification notification = new Notification(sub, gameAsJsonString);
                pushService.send(notification);
            } catch (IOException | ExecutionException | GeneralSecurityException | JoseException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unsubscribe a list of players from the new game notifications
     *
     * @param playerIdList List of playerIds that should be deleted form the subscription map
     */
    private void unsubscribePlayers(List<Player> playerIdList){
        for(Player player : playerIdList){
            pushRegistrations.remove(player.getId());
        }
    }


    /**
     * Send notification to both players
     * @param game Game object
     */
    private void sendNotificationToWhiteAndBlackPlayer(Game game){
        sendPushNotification(game.getWhitePlayerId(), game);
        sendPushNotification(game.getBlackPlayerId(), game);
    }
}
