package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.Image;
import no.ntnu.sjakkarena.repositories.ImageFileRepository;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Objects;

// adapted from: https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@Service
public class FileStorageService {

    @Autowired
    private ImageFileRepository imageFileRepository;

    @Autowired
    private PlayersGameService playersGameService;

    public String basePath = "C:\\Sjakk-Arena\\endPositions\\";  // kan nok være problematisk for andre os


    public void uploadFile(MultipartFile file, int playerId) throws IOException, IllegalStateException, NullPointerException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // Check if name is invalid
            if (fileName.contains("..")) {
                throw new FileSystemException("Invalid name!");
            }
            Path path = Paths.get(basePath + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            fileToDB(fileName, playerId);
        } catch (IOException | NullPointerException | MultipartException e) {
            throw new MultipartException(e + " Bitch");
        }
    }

    private void fileToDB (String filename, int playerId) throws IOException {
        int gameId = playersGameService.getActiveGameId(playerId);
        String timeUploaded = LocalDateTime.now().toString();
        Image image = new Image(filename, gameId, playerId, timeUploaded);
        imageFileRepository.addNewImage(image);
    }

    public Boolean gameHasImages(int gameId) {

    }

}
