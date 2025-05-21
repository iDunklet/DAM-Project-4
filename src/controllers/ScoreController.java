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
            String query = "INSERT INTO points (ID_user, clubs_Types, round_duration, points) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, user.getId());
                stmt.setString(2, clubType.name());
                stmt.setLong(3, durationSeconds);
                stmt.setInt(4, points);
                stmt.executeUpdate();
            }
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