package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.repositories.UserRepository;
import no.ntnu.sjakkarena.utils.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.json.JSONObject;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user
     * @param userJSON A description of the user to be added in json format
     * @return
     */
    @RequestMapping(value="/new-user", method=RequestMethod.PUT)
    public Object registerUser(@RequestBody String userJSON){
        Gson gson = new Gson();
        User user = gson.fromJson(userJSON, User.class);
        try {
            int userId = userRepository.addNewUser(user);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("jwt", JWTHelper.createJWT("user",""+userId));
            return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
        }
        catch(NotAbleToInsertIntoDBException e){
            return new ResponseEntity<>(e.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
