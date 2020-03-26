package no.ntnu.sjakkarena;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        jsonObject.put("tournament_name", tournament.getTournamentName());
        jsonObject.put("tournament_start", tournament.getStart());
        jsonObject.put("tournament_end", tournament.getEnd());
        return jsonObject.toString();
    }

    public String createResponseToNewPlayer(int playerId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt", JWSHelper.createJWS("PLAYER", "" + playerId));
        return jsonObject.toString();
    }
}
