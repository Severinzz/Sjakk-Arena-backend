package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.services.FileStorageService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

// code adapted from: https://www.devglan.com/spring-boot/spring-boot-file-upload-download and https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@RestController
public class FileController {

    @Autowired
    FileStorageService storageService;

    @RequestMapping(value = "/playerFile/Upload",method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        try {
            int playerId = RESTSession.getUserId();
            storageService.uploadFile(file, playerId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File: " + file + " was not found." + e);
        }

    }
}
