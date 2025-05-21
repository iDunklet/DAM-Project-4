package controllers;

import model.userStuff.Score;
import model.userStuff.User;
import model.ClubType;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreController {
    private final DatabaseConnection dbConnection;

    public ScoreController(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void saveScore(User user, ClubType clubType, int strokes, long durationSeconds) throws SQLException {
        int points = calculateScore(strokes, durationSeconds);

        try (Connection conn = dbConnection.getConnection()) {
            // Primero verificamos si ya existe un registro para este usuario
            String checkQuery = "" +
                    "SELECT points " +
                    "FROM points " +
                    "WHERE ID_user = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, user.getId());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Si existe registro, comparamos los puntos
                    int existingPoints = rs.getInt("points");
                    if (points > existingPoints) {
                        // Actualizamos solo si el nuevo puntaje es mayor
                        updateScore(conn, user, clubType, durationSeconds, points);
                    }
                    // Si no es mayor, no hacemos nada
                } else {
                    // Si no existe registro, insertamos uno nuevo
                    insertScore(conn, user, clubType, durationSeconds, points);
                }
            }
        }
    }

    private void insertScore(Connection conn, User user, ClubType clubType, long durationSeconds, int points)
            throws SQLException {
        String insertQuery = "" +
                "INSERT INTO points (ID_user, clubs_Types, round_duration, money, points) " +
                "VALUES (?, ?, ?, 0, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, clubType.name());
            stmt.setLong(3, durationSeconds);
            stmt.setInt(4, points);
            stmt.executeUpdate();
        }
    }

    private void updateScore(Connection conn, User user, ClubType clubType, long durationSeconds, int points)
            throws SQLException {
        String updateQuery = "" +
                "UPDATE points " +
                "SET clubs_Types = ?, round_duration = ?, points = ? " +
                "WHERE ID_user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, clubType.name());
            stmt.setLong(2, durationSeconds);
            stmt.setInt(3, points);
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        }
    }

    public List<Score> getAllScores() throws SQLException {
        List<Score> scores = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection()) {
            String query = """
                SELECT u.id_user, u.name, u.password, 
                       p.id_points, p.clubs_Types, p.round_duration, 
                       p.points, p.Date
                FROM points p
                JOIN users u ON p.ID_user = u.id_User
                """;

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id_user"),
                            rs.getString("name"),
                            rs.getString("password")
                    );

                    ClubType clubType = ClubType.valueOf(rs.getString("clubs_Types"));

                    Score score = new Score(
                            rs.getInt("id_points"),
                            user,
                            clubType,
                            rs.getInt("round_duration"),
                            rs.getInt("points"),
                            rs.getTimestamp("Date")
                    );

                    scores.add(score);
                }
            }
        }
        return scores;
    }

    private int calculateScore(int strokes, long durationSeconds) {
        return (int) (10000.0 / (1 + strokes * 0.5 + durationSeconds * 0.1));
    }
}