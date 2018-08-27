package ru.shoma.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Shoma on 25.05.2018.
 */
public interface SQLTransaction<T> {
    T execute(Connection conn) throws SQLException;
}
