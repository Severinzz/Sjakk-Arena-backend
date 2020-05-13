package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.StorageProperties;
import no.ntnu.sjakkarena.data.Image;
import no.ntnu.sjakkarena.exceptions.StorageException;
import no.ntnu.sjakkarena.repositories.ImageFileRepository;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

// adapted from: https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/ &
// https://spring.io/guides/gs/uploading-files/

@Service
public class FileStorageService {

    @Autowired
    private ImageFileRepository imageFileRepository;

    @Autowired
    private PlayersGameService playersGameService;

    private Path rootLocation; // Specific path for upload folder, irrelevant of OS.

    @Autowired
    public void FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    /**
     * Saves a file to local and registers to database
     *
     * @param file     file to save
     * @param playerId id of player uploading.
     * @throws IOException
     * @throws IllegalStateException
     * @throws NullPointerException
     */
    public void saveFile(MultipartFile file, int playerId) throws IOException, IllegalStateException, NullPointerException {
        String fileName = getFileName(file);
        try {
            // Check if name is invalid
            if (fileName.contains("..")) {
                throw new FileSystemException("Invalid name!");
            }
            Path savePath = provideUniquePath(file);
            if (!savePath.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
            fileToDB(savePath.getFileName().toString(), playerId);
        } catch (IOException | NullPointerException e) {
            throw new IOException(e);
        }
    }

    /**
     * Returns the specified file's name
     *
     * @param file
     * @return the specified file's name
     */
    private String getFileName(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }

    /**
     * Returns a unique path for the specified file
     *
     * @param file
     * @return a unique path for the specified file
     */
    private Path provideUniquePath(MultipartFile file) {
        int i = 1;
        String fileName = file.getOriginalFilename();
        Path path = getSavePath(fileName);
        while (Files.exists(path)) {
            path = getSavePath("(" + i + ")" + fileName);
            i++;
        }
        return path;
    }


    /**
     * Return the path where the file with the given filename is saved
     *
     * @param filename
     * @return the path where the file with the given filename is saved
     */
    private Path getSavePath(String filename) {
        // Adapted from: https://github.com/spring-guides/gs-uploading-files/pull/68/files
        return this.rootLocation.resolve(
                Paths.get(filename))
                .normalize().toAbsolutePath();
    }

    /**
     * Input file details to database
     *
     * @param filename name of file
     * @param playerId id of player uploading file
     * @throws IOException
     */
    private void fileToDB(String filename, int playerId) throws IOException {
        int gameId = playersGameService.getActiveGameId(playerId);
        String timeUploaded = LocalDateTime.now().toString();
        Image image = new Image(filename, gameId, playerId, timeUploaded);
        imageFileRepository.addNewImage(image);
    }

    /**
     * Gets images belonging to game
     *
     * @param gameId id of game to find images for.
     * @return list of images belonging to game
     */
    public List<Image> fetchGameImages(int gameId) {
        List<Image> images = imageFileRepository.findImagesToGameId(gameId);
        return images;
    }

    /**
     * Gets the path for folder to upload
     *
     * @return path of folder
     */
    public String getPath() {
        return rootLocation.toString();
    }
}
