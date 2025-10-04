package org.example.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseInterface {
    Connection getConnection() throws SQLException;
    void closeConnection() throws SQLException;
}
