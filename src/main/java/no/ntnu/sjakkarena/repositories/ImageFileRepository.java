package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Image;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

// code from: https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/

@Repository
public interface ImageFileRepository extends JpaRepository<Image, String> {

}