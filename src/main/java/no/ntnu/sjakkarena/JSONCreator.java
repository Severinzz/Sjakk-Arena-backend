package no.ntnu.sjakkarena;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.utils.JWSHelper;
import org.json.JSONObject;

public class JSONCreator {

    ObjectMapper objectMapper = new ObjectMapper();

    public String writeValueAsString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String filterPlayersTournamentInformationAndReturnAsJson(Tournament tournament) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", tournament.getTournamentName());
        jsonObject.put("started", tournament.isActive());
        jsonObject.put("start", tournament.getStart());
        jsonObject.put("end", tournament.getEnd());
        return jsonObject.toString();
    }

    public String filterPlayerInformationAndReturnAsJson(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", player.getName());
        jsonObject.put("points", player.getPoints());
        return jsonObject.toString();
    }

    public String filterGameInformationAndReturnAsJson(Game game, int playerId){
        if (game instanceof GameWithPlayerNames){
            return filterGameWithPlayerNamesInformationAndReturnAsJson(game, playerId);
        } else {
            return writeValueAsString(game);
        }
    }

    private String filterGameWithPlayerNamesInformationAndReturnAsJson(Game game, int playerId){
        JSONObject jsonObject = new JSONObject();
        if (game.getWhitePlayerId() == playerId){
            jsonObject.put("opponent", ((GameWithPlayerNames) game).getBlackPlayerName());
            jsonObject.put("opponent_id", game.getBlackPlayerId());
            jsonObject.put("colour", "Hvit");
        } else {
            jsonObject.put("opponent", ((GameWithPlayerNames) game).getWhitePlayerName());
            jsonObject.put("opponent_id", game.getWhitePlayerId());
            jsonObject.put("colour", "Sort");
        }
        jsonObject.put("table", game.getTable());
        return jsonObject.toString();
    }

    /**
     * Get the message to be returned to the client
     *
     * @param tournament The newly created tournament
     * @return information to be returned to the client
     */
    public String createResponseToNewTournament(Tournament tournament) {
        String jws = JWSHelper.createJWS("TOURNAMENT", "" + tournament.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt", jws);
        jsonObject.put("tournament_id", tournament.getId());
        return jsonObject.toString();
    }

    public String createResponseToNewPlayer(int playerId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt", JWSHelper.createJWS("PLAYER", "" + playerId));
        return jsonObject.toString();
    }

    public String createResponseToTournamentStateSubscriber(boolean active){
        return "{ \"active\": " + active + " }";
    }

    public String createResponseToPlayerPointsSubscriber(double points) { return "{ \"points\": " + points + " }"; }

    public String createResponseToResultSubscriber(double result, int gameId, boolean suggestedResult, boolean validResult){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("game_id", gameId);
        jsonObject.put("result_is_suggested", suggestedResult);
        jsonObject.put("result_is_valid", validResult);
        return jsonObject.toString();
    }
}
