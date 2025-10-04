package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class PostgreSQLDatabase implements DatabaseInterface{
    private final String db_url;
    private final String db_user;
    private final String db_password;

    private Connection connection;

    public static PostgreSQLDatabase instance;

    private PostgreSQLDatabase() {
        Dotenv dotenv = Dotenv.load();
        this.db_url = dotenv.get("DATABASE_URL");
        this.db_user = dotenv.get("DB_USER");
        this.db_password = dotenv.get("DB_PASSWORD");
    }

    public static synchronized PostgreSQLDatabase getInstance() {
        if (instance == null) {
            instance = new PostgreSQLDatabase();
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(db_url, db_user, db_password);
        }
        return connection;
    }

    @Override
    public void closeConnection() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
