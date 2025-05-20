package controllers.dbstuff;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public static Object[][] getAllUsersWithPoints() {
        List<Object[]> rows = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = """
            SELECT u.name, u.password, 
                   p.id_points, p.clubs_Types, p.round_duration, 
                   p.money, p.points, p.Date
            FROM points p
            JOIN users u ON p.ID_user = u.id_User
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String username = rs.getString("name");
                    String password = rs.getString("password");
                    int idPoints = rs.getInt("id_points");
                    String clubTypeStr = rs.getString("clubs_Types");
                    ClubType clubType = ClubType.valueOf(clubTypeStr);
                    int roundDuration = rs.getInt("round_duration");
                    BigDecimal dinero = rs.getBigDecimal("money");
                    int points = rs.getInt("points");
                    Timestamp fecha = rs.getTimestamp("Date");

                    rows.add(new Object[]{
                            username, password, idPoints, clubType, roundDuration,
                            dinero, points, fecha
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }
        return data;
    }



}


