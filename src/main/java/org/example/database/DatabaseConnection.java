package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String URL = "jdbc:postgresql://localhost:5432/bank_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres123";


    private static Connection connection;

    private DatabaseConnection() {}


    // Nouvelle connexion à chaque appel
    public static Connection getInstance() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
