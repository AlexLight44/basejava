package webapp.storage.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlHelper<T>{
    T execute (PreparedStatement preparedStatement) throws SQLException;
}
