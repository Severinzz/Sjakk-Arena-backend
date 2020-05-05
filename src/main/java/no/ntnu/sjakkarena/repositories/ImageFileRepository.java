package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Image;
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


@Repository
public class ImageFileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Adds a image name to the database
     * @param image to be added
     */
    public void addNewImage(Image image) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            //Adapted code from https://stackoverflow.com/questions/12882874/how-can-i-get-the-autoincremented-id-when-i-insert-a-record-in-a-table-via-jdbct
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement(
                                            "INSERT INTO `sjakkarena`.`image` " +
                                                    "(playerId, gameId, fileName, time_Uploaded)" +
                                            "VALUES (" + "'" + image.getPlayerId() + "', '" + image.getGameId() +
                                                      "', '" + image.getFilename() + "', '" + image.getTimeUploaded() +
                                                      "')", new String[]{"id"});
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