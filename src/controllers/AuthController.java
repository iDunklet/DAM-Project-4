package controllers;

import model.userStuff.User;
import utils.DatabaseConnection;

import java.sql.*;

public class AuthController {
    private final DatabaseConnection dbConnection;

    public AuthController(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public User loginOrRegister(String username, String password) throws SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            // Check if user exists
            User user = findUser(conn, username, password);

            if (user == null) {
                // Register new user
                user = registerUser(conn, username, password);
            }

            return user;
        }
    }

    private User findUser(Connection conn, String username, String password) throws SQLException {
        String query = "SELECT id_user FROM users WHERE name = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id_user"), username, password);
            }
        }
        return null;
    }

    private User registerUser(Connection conn, String username, String password) throws SQLException {
        String query = "INSERT INTO users (name, password) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new User(generatedKeys.getInt(1), username, password);
                }
            }
        }
        throw new SQLException("Failed to register user");
    }
}
