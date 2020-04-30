package no.ntnu.sjakkarena.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    public void uploadFile(MultipartFile file) throws IOException, IllegalStateException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get("C:\\Sjakk-Arena\\endPositions\\" + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }
}
