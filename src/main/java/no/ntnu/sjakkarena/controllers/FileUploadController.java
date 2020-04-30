package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

// code adapted from: https://www.devglan.com/spring-boot/spring-boot-file-upload-download

@RestController
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;

    @PostMapping("/playerFile/upload")
    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        try {
            fileUploadService.uploadFile(file);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File: " + file + " was not found." + e);
        }

    }
}
