package org.example;

import org.example.database.DatabaseConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connexion à la base réussie !");
        } else {
            System.out.println("Échec de la connexion.");
        }
    }
}
