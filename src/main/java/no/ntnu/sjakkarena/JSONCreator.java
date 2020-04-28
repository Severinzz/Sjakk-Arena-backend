package no.ntnu.sjakkarena;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.utils.JWSHelper;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Creates JSONs
 */
@Component
public class JSONCreator {

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Returns a JSON representation of the input object
     *
     * @param object The object to represent with a JSON
     * @return a JSON representation of the input object
     */
    public String writeValueAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Filters information about a player's tournament and return the relevant tournament information as a JSON string
     *
     * @param tournament The tournament to filter information from
     * @return the relevant tournament information as a JSON string
     */
    public String filterPlayersTournamentInformationAndReturnAsJson(Tournament tournament) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", tournament.getTournamentName());
        jsonObject.put("started", tournament.isActive());
        jsonObject.put("start", tournament.getStart());
        jsonObject.put("end", tournament.getEnd());
        return jsonObject.toString();
    }

    /**
     * Filters information about a player and return the relevant information as a JSON string
     *
     * @param player The player to filter information from
     * @return the relevant player information as a JSON string
     */
    public String filterPlayerInformationAndReturnAsJson(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", player.getName());
        jsonObject.put("points", player.getPoints());
        return jsonObject.toString();
    }

    /**
     * Filters information about a game and return the relevant information as a JSON string
     *
     * @param game     The game to filter information from
     * @param playerId The id of the player receiving the information
     * @return The relevant information as a JSON string
     */
    public String filterGameInformationAndReturnAsJson(Game game, int playerId) {
        if (game instanceof GameWithPlayerNames) {
            return filterGameWithPlayerNamesInformationAndReturnAsJson((GameWithPlayerNames) game, playerId);
        } else {
            return writeValueAsString(game);
        }
    }

    /**
     * Filters information about a game with player names and return the relevant information as a JSON string
     *
     * @param game     The game with player names to filter information from
     * @param playerId The id of the player receiving the information
     * @return The relevant information as a JSON string
     */
    private String filterGameWithPlayerNamesInformationAndReturnAsJson(GameWithPlayerNames game, int playerId) {
        JSONObject jsonObject = new JSONObject();
        if (game.getWhitePlayerId() == playerId) {
            jsonObject.put("opponent", game.getBlackPlayerName());
            jsonObject.put("opponent_id", game.getBlackPlayerId());
            jsonObject.put("colour", "Hvit");
        } else {
            jsonObject.put("opponent", game.getWhitePlayerName());
            jsonObject.put("opponent_id", game.getWhitePlayerId());
            jsonObject.put("colour", "Sort");
        }
        jsonObject.put("table", game.getTable());
        jsonObject.put("active", game.isActive());
        return jsonObject.toString();
    }

    /**
     * Creates the JSON response be returned to the new tournament
     *
     * @param tournament The newly created tournament
     * @return information to be returned to the tournament
     */
    public String createResponseToNewTournament(Tournament tournament) {
        String jws = JWSHelper.createJWS("TOURNAMENT", "" + tournament.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt", jws);
        jsonObject.put("tournament_id", tournament.getId());
        return jsonObject.toString();
    }

    /**
     * Creates the JSON response be returned to the new player
     *
     * @param playerId The id of the newly created player
     * @return information to be returned to the player
     */
    public String createResponseToNewPlayer(int playerId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt", JWSHelper.createJWS("PLAYER", "" + playerId));
        return jsonObject.toString();
    }

    /**
     * Returns a response to a user subscribing to the tournament active state (STOMP) destination
     *
     * @param active Whether the tournament is active
     * @return a response to a user subscribing to the tournament active state (STOMP) destination
     */
    public String createResponseToTournamentStateSubscriber(boolean active) {
        return "{ \"active\": " + active + " }";
    }

    /**
     * Returns a response to a player subscribing to the player's points (STOMP) destination
     *
     * @param points The player's points
     * @return a response to a player subscribing to the player's points (STOMP) destination
     */
    public String createResponseToPlayerPointsSubscriber(double points) {
        return "{ \"points\": " + points + " }";
    }

    /**
     * Returns a response to a user subscribing to the result (STOMP) destination
     *
     * @param result            A result
     * @param gameId            The id of the game the result is associated with
     * @param opponentsDisagree Whether the opponents playing the game disagrees on the result
     * @param validResult       Whether the result is regarded as valid
     * @return a response to a user subscribing to the result (STOMP) destination
     */
    public String createResponseToResultSubscriber(Double result, Integer gameId, boolean opponentsDisagree,
                                                   boolean validResult) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("suggested_result", result);
        jsonObject.put("game_id", gameId);
        jsonObject.put("opponents_disagree", opponentsDisagree);
        jsonObject.put("valid", validResult);
        return jsonObject.toString();
    }
}
