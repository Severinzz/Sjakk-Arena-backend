package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.utils.DBInteractionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addNewUser(User user){
        String dbUpdateString = DBInteractionHelper.toDatabaseUpdateString(user.getName(),
                user.getTournament());
        try {
            jdbcTemplate.update("INSERT INTO sjakkarena.user (`name`, `tournament`) VALUES (" + dbUpdateString + ")");
            return 0;
        }
        catch(DataAccessException e){
            throw new NotAbleToInsertIntoDBException("Could not add user to database");
        }
    }
}
