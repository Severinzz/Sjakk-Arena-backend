package no.ntnu.sjakkarena.controllers.restcontrollers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.exceptions.NameAlreadyExistsException;
import no.ntnu.sjakkarena.services.UnauthenticatedUserService;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.EmailSender;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.IDGenerator;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import no.ntnu.sjakkarena.utils.Validator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests from users who don't have a token to identify them.  All methods in this class will return a
 * token to the user.
 */
@RestController
public class UnauthenticatedUserRESTController {

    // TODO move business logic to service class


    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UnauthenticatedUserService unauthenticatedUserService;

    /**
     * Register a new user
     *
     * @param playerJSON A description of the user to be added in json format
     * @return
     */
    @RequestMapping(value = "/new-player", method = RequestMethod.POST)
    public ResponseEntity<String> registerPlayer(@RequestBody String playerJSON) {
        Gson gson = new Gson();
        Player player = gson.fromJson(playerJSON, Player.class);
        try {
            String responseMessage = unauthenticatedUserService.addNewPlayer(player);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            return new ResponseEntity<>("Couldn't add player to database", HttpStatus.BAD_REQUEST);
        } catch (NameAlreadyExistsException e) {
            return new ResponseEntity<>("Name already take, try a new one!", HttpStatus.CONFLICT); // frontend leter etter kode 409
        }
    }

    /**
     * Register a tournament
     *
     * @param tournamentJSON tournament description in json format
     * @return HTTP status 200 ok if successfully added + jwt and tournamentID,
     * HTTP status 422 UNPROCESSABLE_ENTITY otherwise
     */
    @RequestMapping(value = "/new-tournament",
            method = RequestMethod.POST)
    public ResponseEntity<String> registerTournament(@RequestBody String tournamentJSON) {
        Tournament tournament = new Gson().fromJson(tournamentJSON, Tournament.class);
        try {
            Validator.validateTournament(tournament);
            addTournamentIDs(tournament);
            tournamentRepository.addNewTournament(tournament);
            //sendEmailToTournamentAdmin(tournament); //TODO remove to send email
            return new ResponseEntity<>(getToClientJSON(tournament), HttpStatus.OK);
        } catch (NotAbleToUpdateDBException | ImproperlyFormedDataException | NullPointerException e) {
            return new ResponseEntity<>("Couldn't register tournament", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Get the information to be returned to the client
     *
     * @param tournament The newly created tournament
     * @return information to be returned to the client
     */
    private String getToClientJSON(Tournament tournament) {
        String jws = JWSHelper.createJWS("TOURNAMENT", "" + tournament.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jwt", jws);
        jsonObject.put("tournament_id", tournament.getId());
        return jsonObject.toString();
    }

    /**
     * Adds tournament id and adminUUID to the tournament
     *
     * @param tournament the tournament to be given IDs
     */
    private void addTournamentIDs(Tournament tournament) {
        tournament.setId(IDGenerator.generateTournamentID());
        tournament.setAdminUUID(IDGenerator.generateAdminUUID());
    }

    /**
     * Sends an email to the tournament admin containing the adminUUID
     *
     * @param tournament a tournament
     */
    private void sendEmailToTournamentAdmin(Tournament tournament) {
        EmailSender emailSender = new EmailSender();
        emailSender.sendEmail(tournament.getAdminEmail(), tournament.getTournamentName(),
                "Her er din adminUUID: " + tournament.getAdminUUID() +
                        ". Bruk denne til å gå til din turneringsside");
    }

    /**
     * "Sign in" using adminUUID.
     *
     * @param UUID the universally unique identifier of the tournament
     * @return 200 OK + a jwt with subject: "TOURNAMENT" and tournament id as an id.
     * 400 Bad Request if there is no tournament associated with the UUID.
     */
    @RequestMapping(value = "/sign-in/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<String> signIn(@PathVariable(name = "uuid") String UUID) {
        Tournament tournament = tournamentRepository.getTournament(UUID);
        if (tournament.getAdminUUID() == null) {
            return new ResponseEntity<>("adminUUID is incorrect", HttpStatus.BAD_REQUEST);
        } else {
            //Create JWS
            String jws = JWSHelper.createJWS("TOURNAMENT", "" + tournament.getId());
            //JSON with JWS and tournamentInformation
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jwt", jws);
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
    }
}
