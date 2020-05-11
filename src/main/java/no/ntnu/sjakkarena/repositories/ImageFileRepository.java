package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Image;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.mappers.ImageRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
public class ImageFileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Image> imageRowMapper = new ImageRowMapper();

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

    /**
     * Find names of images belonging to specified game
     * @param gameId id of game to find images for
     */
    public List<Image> findImagesToGameId(int gameId) {
        String sql = "select * from `sjakkarena`.`image`" +
                " where gameId = " + gameId;
        try {
            List<Image> images = jdbcTemplate.query(sql, imageRowMapper);
            return images;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("This game does not have any images.");
        }
    }

}