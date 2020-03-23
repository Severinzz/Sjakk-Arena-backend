package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.EmailSender;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.NewPlayerAddedEvent;
import no.ntnu.sjakkarena.exceptions.NameAlreadyExistsException;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.IDGenerator;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import no.ntnu.sjakkarena.utils.Validator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UnauthenticatedUserService {

    // TODO use events instead
    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public String addNewPlayer(Player player) {
        if (playerRepository.doesPlayerExist(player)){
            throw new NameAlreadyExistsException("Name already take, try a new one!");
        }
        try {
            player.setIcon(PlayerIcons.getRandomFontAwesomeIcon());
            int userId = playerRepository.addNewPlayer(player);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("jwt", JWSHelper.createJWS("PLAYER", "" + userId));
            onNewPlayerAdd(player.getTournamentId());
            return jsonResponse.toString();
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String handleAddTournamentRequest(Tournament tournament) {
        Validator.validateThatStartIsBeforeEnd(tournament);
        addTournamentIDs(tournament);
        tournamentRepository.addNewTournament(tournament);
        //sendEmailToTournamentAdmin(tournament); //Remove comment to send email
        return getMessageToClient(tournament);
    }

    public String signIn(String adminUUID){
        try {
            Tournament tournament = tournamentRepository.getTournament(adminUUID);
            return getMessageToClient(tournament);
        } catch(NotInDatabaseException e){
            throw e;
        }
    }

    private void onNewPlayerAdd(int tournamentId) {
        tournamentSubscriberHandler.sendPlayerList(tournamentId);
        if (tournamentRepository.getTournament(tournamentId).isActive()) {
            NewPlayerAddedEvent newPlayerAddedEvent = new NewPlayerAddedEvent(this, tournamentId);
            applicationEventPublisher.publishEvent(newPlayerAddedEvent);
        }
    }

    /**
     * Get the message to be returned to the client
     *
     * @param tournament The newly created tournament
     * @return information to be returned to the client
     */
    private String getMessageToClient(Tournament tournament) {
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
}
