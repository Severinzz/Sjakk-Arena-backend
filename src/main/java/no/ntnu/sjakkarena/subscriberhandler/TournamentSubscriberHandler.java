package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.GamesCreatedEvent;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.Collection;

public class TournamentSubscriberHandler extends SubscriberHandler{

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Sends all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    public void sendPlayerList(int tournamentId) {
        try {
            // TODO remove tournament repository.getPl... use events instead
            Collection<Player> players = playerRepository.getPlayersInTournament(tournamentId);
            sendToSubscriber(tournamentId,"/queue/tournament/players",
                    jsonCreator.writeValueAsString(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("a list of players", e);
        }
    }

    /**
     * Sends the tournaments leaderboard
     *
     * @param tournamentId The id of the tournament
     */
    public void sendLeaderBoard(int tournamentId) {
        try {
            // TODO remove tournament repository.getPl... use events instead
            Collection<Player> players = tournamentRepository.getPlayersSortedByPoints(tournamentId);
            sendToSubscriber(tournamentId, "/queue/tournament/leaderboard",
                    jsonCreator.writeValueAsString(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("a leaderboard", e);
        }
    }

    /**
     *
     * @param gamesCreatedEvent
     */
    @EventListener
    public void onGamesCreated(GamesCreatedEvent gamesCreatedEvent) {
        try {
            sendToSubscriber(gamesCreatedEvent.getTournamentId(), "/queue/tournament/games",
                    jsonCreator.writeValueAsString(gamesCreatedEvent.getActiveGames()));
        }
        catch(NullPointerException e){
            printNotSubscribingErrorMessage("new games", e);
        }
    }
}
