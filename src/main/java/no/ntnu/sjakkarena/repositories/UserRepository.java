package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.utils.DBInteractionHelper;
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
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addNewUser(User user){
        String dbUpdateString = DBInteractionHelper.toDatabaseUpdateString(user.getName(),
                user.getTournament());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            //code from https://stackoverflow.com/questions/12882874/how-can-i-get-the-autoincremented-id-when-i-insert-a-record-in-a-table-via-jdbct
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement("INSERT INTO sjakkarena.user (`name`, `tournament`)" +
                                            " VALUES (" + dbUpdateString + ")", new String[] {"id"});
                            return ps;
                        }
                    },
                    keyHolder);
            return keyHolder.getKey().intValue();
        }
        catch(DataAccessException e){
            throw new NotAbleToInsertIntoDBException("Could not add user to database. Possible reasons: \n" +
                    "1. User is already registered in database");
        }
    }
}
