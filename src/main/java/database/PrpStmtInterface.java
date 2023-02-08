package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An Interface for a prepared statement lambda abstract.
 */
public interface PrpStmtInterface {
    /**
     * An abstract function for a lambda call to create a MySQL prepared statement with varargs.
     * @param qry The query string.
     * @param str A vararg of Strings.
     * @return Return a prepared statement.
     * @throws SQLException The catch will usually be within a try-with-resources statement.
     */
    PreparedStatement ps(String qry, String... str) throws SQLException;

}
