package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Image;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.mappers.ImageRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ImageFileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Image> imageRowMapper = new ImageRowMapper();

    /**
     * Adds a image name to the database
     *
     * @param image to be added
     */
    public void addNewImage(Image image) {
        try {
            jdbcTemplate.update("INSERT INTO `sjakkarena`.`image` " +
                    "(playerId, gameId, fileName, time_Uploaded)" +
                    "VALUES (" + "'" + image.getPlayerId() + "', '" + image.getGameId() +
                    "', '" + image.getFilename() + "', '" + image.getTimeUploaded() +
                    "')");
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not add image to database. Possible reasons: \n" +
                    "1. Image is already registered in database \n" +
                    "2. Name/value pairs in JSON are missing");
        }
    }

    /**
     * Return names of images belonging to specified game
     *
     * @param gameId id of game to find images for
     * @return images names of images belonging to specified game
     */
    public List<Image> findImagesToGameId(int gameId) {
        String sql = "select * from `sjakkarena`.`image`" +
                " where gameId = " + gameId;
        try {
            List<Image> images = jdbcTemplate.query(sql, imageRowMapper);
            if (images.size() == 0) {
                throw new NotInDatabaseException("This game does not have any images");
            }
            return images;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("This game does not have any images.");
        }
    }

    /**
     * Returns true if there is a image associated with the specified game
     *
     * @param game
     * @return true if there is a image associated with the specified game
     */
    public boolean hasImage(Game game) {
        List<Image> images = jdbcTemplate.query("SELECT * FROM sjakkarena.image WHERE gameId = " + game.getGameId(),
                imageRowMapper);
        return !images.isEmpty();
    }
}
