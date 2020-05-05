package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.services.FileStorageService;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

// code adapted from: https://www.devglan.com/spring-boot/spring-boot-file-upload-download and https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@RestController
public class FileController {

    @Autowired
    FileStorageService storageService;
    WebSocketSession webSocketSession;

    @PostMapping("/player/File/Upload")
    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile file, Authentication authentication) throws IOException {
        try {
            int playerId = webSocketSession.getUserId(authentication);
            storageService.uploadFile(file, playerId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File: " + file + " was not found." + e);
        }

    }
}
