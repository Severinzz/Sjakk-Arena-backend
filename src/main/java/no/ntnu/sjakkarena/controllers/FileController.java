package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.data.Image;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// code adapted from: https://www.devglan.com/spring-boot/spring-boot-file-upload-download and https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@RestController
public class FileController {

    @Autowired
    FileStorageService storageService;

    @Autowired
    private PlayersGameService playersGameService;

    public String basePath = "C:\\Sjakk-Arena\\endPositions\\";

    @RequestMapping(value = "/playerFile/Upload", method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("Image")MultipartFile file) throws IOException {
//        Image image = new Image();
//        int playerId = RESTSession.getUserId();
//        String timeUploaded = LocalDateTime.now().toString();
//        int gameId = playersGameService.getActiveGameId(playerId);
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        image.setTimeUploaded(timeUploaded);
//        image.setFileName(fileName);
//        image.setPlayerId(playerId);
//        image.setGameId(gameId);
//        DocumentDAO.save(image);
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/files/download/")
//                .path(fileName).path("/db")
//                .toUriString();
//        return ResponseEntity.ok(fileDownloadUri);
        try {
            int playerId = RESTSession.getUserId();
            storageService.uploadFile(file, playerId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("image: " + file + " was not found." + e);
        } catch (MultipartException e) {
            throw new MultipartException(e + " Not a multipartfile.");
        }
    }

    @RequestMapping(value = "/playerFile/Download/{gameId}", method = RequestMethod.GET, produces="application/zip")
    public void downloadFiles(@PathVariable ("gameId") int gameId, HttpServletResponse response) throws IOException {
        List<Image> images = storageService.fetchGameImages(gameId);
        Path path = Paths.get(basePath + images);
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (Image image : images) {
            FileSystemResource resource = new FileSystemResource(basePath + image.getFilename());
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "Game " + gameId + " images" + "\"");
/*        for (int i = 0; i < images.size(); i++) {
            try {
                resource = new UrlResource(path.toUri());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }*/
        /*return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);*/
    }
}
