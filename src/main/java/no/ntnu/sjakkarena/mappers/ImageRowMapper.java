package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.Image;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRowMapper implements RowMapper<Image> {
    /**
     * Maps results from a database into a Image object
     *
     * @param rs
     * @param i
     * @return A image
     * @throws SQLException
     *
     */
    @Override
    public Image mapRow(ResultSet rs, int i) throws SQLException {
        Image image = new Image(rs.getInt("image_id"), rs.getInt("gameId"),
                 rs.getInt("playerId"), rs.getString("timeUploaded"),  rs.getString("fileName"));
        return image;
    }
}
