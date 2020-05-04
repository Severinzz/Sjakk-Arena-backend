package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.repositories.ImageFileRepository;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

// adapted from: https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@Service
public class FileStorageService {

    @Autowired
    private static JdbcTemplate jdbcTemplate;


    public void uploadFile(MultipartFile file, int playerId) throws IOException, IllegalStateException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // Check if name is invalid
            if (fileName.contains("..")) {
                throw new FileSystemException("Invalid name!");
            }
            Path path = Paths.get("C:\\Sjakk-Arena\\endPositions\\" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            fileToDB(file, playerId);
        } catch (IOException e) {
        }
    }

    private void fileToDB (MultipartFile file, int playerId) {
        PlayersGameService playersGameService = new PlayersGameService();
        //int gameId = playersGameService.getActiveGameId(playerId);
        int gameId = 8;
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        long imageSizeMB = file.getSize()/1048576;
        ImageFileRepository.addNewImage(fileName, playerId, gameId, imageSizeMB);
    }

}
