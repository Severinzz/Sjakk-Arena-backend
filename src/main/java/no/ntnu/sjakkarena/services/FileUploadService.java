package no.ntnu.sjakkarena.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {

    public void uploadFile(MultipartFile file) throws IOException, IllegalStateException {
        file.transferTo(new File("C:\\Sjakk-Arena\\endPositions\\" + file.getOriginalFilename()));
    }
}
