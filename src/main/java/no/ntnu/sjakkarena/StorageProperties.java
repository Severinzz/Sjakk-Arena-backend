package no.ntnu.sjakkarena;

import org.springframework.context.annotation.Configuration;

// code from: https://github.com/spring-guides/gs-uploading-files

@Configuration
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir"; // Folder in project directory

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
