package ru.shoma.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecutor<T> {
    T executeAndGet(PreparedStatement statement) throws SQLException;
}
