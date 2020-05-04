package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;


@Repository
public class ImageFileRepository {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    /**
     * Adds a image name to the database
     * @param fileName name of image
     * @param playerId id of player uploading
     * @param gameId id of game image is from
     * @param imageSize size of image
     */
    public static void addNewImage(String fileName, int playerId, int gameId, long imageSize) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String time = LocalDateTime.now().toString();
            //Adapted code from https://stackoverflow.com/questions/12882874/how-can-i-get-the-autoincremented-id-when-i-insert-a-record-in-a-table-via-jdbct
            jdbcTemplate.update( // = null problem
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement("INSERT INTO `sjakkarena`.`image` (`playerId`, " +
                                            "`gameId`, `imageSize`, `fileName`, `time_Uploaded`) " +
                                            "VALUES (\"" + playerId + "\", " + gameId + "\"," + imageSize + "\"," + fileName + "\"," + time +")", new String[]{"id"});
                            return ps;
                        }
                    },
                    keyHolder);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new TroubleUpdatingDBException("Could not add image to database. Possible reasons: \n" +
                    "1. Image is already registered in database \n" +
                    "2. Name/value pairs in JSON are missing");
        }
    }

}