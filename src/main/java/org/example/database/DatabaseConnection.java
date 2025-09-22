package org.example.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

        private static final String URL = "jdbc:postgresql://localhost:5432/bank_db"; // ta base
        private static final String USER = "postgres"; // ton user PostgreSQL
        private static final String PASSWORD = "postgres123"; // ton mot de passe

        public static Connection getConnection() {
            try {
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
                return null;
            }
        }


}
