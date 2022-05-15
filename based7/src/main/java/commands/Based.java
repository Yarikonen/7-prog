package commands;

import java.sql.Connection;
import java.sql.SQLException;

public interface Based {
    void basedCommand(Connection c, String tableName) throws SQLException;
}
