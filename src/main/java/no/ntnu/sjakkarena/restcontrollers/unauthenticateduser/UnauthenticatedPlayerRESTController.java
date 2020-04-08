package no.ntnu.sjakkarena.restcontrollers.unauthenticateduser;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NameAlreadyExistsException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.unauthenticateduser.UnauthenticatedPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests from users who don't have a token to identify them.  All methods in this class will return a
 * token to the user.
 */
@RestController
public class UnauthenticatedPlayerRESTController {

    @Autowired
    private UnauthenticatedPlayerService unauthenticatedPlayerService;

    /**
     * Register a new user
     *
     * @param player
     * @return
     */
    @RequestMapping(value = "/new-player", method = RequestMethod.POST)
    public ResponseEntity<String> registerPlayer(@RequestBody Player player) {
        try {
            String responseMessage = unauthenticatedPlayerService.handleAddPlayerRequest(player);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NameAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // frontend leter etter kode 409
        }
    }
}
