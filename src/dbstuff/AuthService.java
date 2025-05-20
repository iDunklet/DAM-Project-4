package dbstuff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.*;

public class AuthService {
    private static final String URL = "jdbc:mysql://localhost:3306/golf_game";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    public static boolean loginOrCreateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Usuario existe
                return true;
            } else {
                // Usuario no existe, insertarlo
                String insert = "INSERT INTO users (name, password) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insert);
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


