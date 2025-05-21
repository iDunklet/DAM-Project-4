package model.userStuff;

import model.ClubType;
import model.userStuff.User;

import java.sql.Timestamp;

public class Score {
    private int id;
    private User user;
    private ClubType clubType;
    private int roundDuration;
    private int points;
    private Timestamp date;
    private int money;

    public Score(int id, User user, ClubType clubType, int roundDuration, int points, Timestamp date) {
        this.id = id;
        this.user = user;
        this.clubType = clubType;
        this.roundDuration = roundDuration;
        this.points = points;
        this.date = date;
    }

    // Getters
    public User getUser() { return user; }
    public ClubType getClubType() { return clubType; }

    public int getId() {
        return id;
    }

    public int getRoundDuration() {
        return roundDuration;
    }

    public int getPoints() {
        return points;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getMoney() {
        return money;
    }
}
