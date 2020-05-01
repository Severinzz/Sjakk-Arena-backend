package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.Image;
import no.ntnu.sjakkarena.repositories.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// adapted from: https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@Service
public class FileStorageService {

    public void uploadFile(MultipartFile file) throws IOException, IllegalStateException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get("C:\\Sjakk-Arena\\endPositions\\" + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Image image = new Image(fileName, file.getContentType(), file.getBytes());

        return ImageFileRepository.save(image);
    }
}
