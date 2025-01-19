package panificadora.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/panificadora";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() throws SQLException {
        try {
            // Registrar o driver JDBC do PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do PostgreSQL não encontrado!");
            e.printStackTrace();
        }
        // Retornar a conexão
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
