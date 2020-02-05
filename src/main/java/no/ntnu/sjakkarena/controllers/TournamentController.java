package no.ntnu.sjakkarena.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class TournamentController {

    @RequestMapping("/new-tournament")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
