package no.ntnu.sjakkarena;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
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
}
