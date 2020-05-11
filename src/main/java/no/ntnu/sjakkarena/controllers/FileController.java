package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.data.Image;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.FileStorageService;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// code adapted from: https://www.devglan.com/spring-boot/spring-boot-file-upload-download and https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@RestController
public class FileController {

    @Autowired
    FileStorageService storageService;

    @Autowired
    private PlayersGameService playersGameService;

    @RequestMapping(value = "/playerFile/Upload", method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("Image")MultipartFile file) throws IOException {
        try {
            int playerId = RESTSession.getUserId();
            storageService.saveFile(file, playerId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("image: " + file + " was not found." + e);
        } catch (MultipartException e) {
            throw new MultipartException(e + " Not a multipartfile.");
        }
    }

    @RequestMapping(value = "/playerFile/Download/{gameId}", method = RequestMethod.GET, produces="application/zip")
    public void downloadFiles(@PathVariable ("gameId") int gameId, HttpServletResponse response) throws IOException {
        try {
            List<Image> images = storageService.fetchGameImages(gameId);

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (Image image : images) {
            FileSystemResource resource = new FileSystemResource(storageService.getPath() + "\\" + image.getFilename());
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; Zip containing images.");
        } catch (NotInDatabaseException e) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
