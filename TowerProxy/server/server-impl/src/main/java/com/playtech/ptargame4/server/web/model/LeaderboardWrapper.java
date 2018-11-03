package com.playtech.ptargame4.server.web.model;

import com.playtech.ptargame4.server.database.model.EloRating;

public class LeaderboardWrapper {
    private final String name;
    private final int userId;
    private final int eloRating;
    private final int matches;
    private final int goals;
    private final int bulletHits;
    private final int totalScore;
    private final int ballTouches;
    private final int boostTouches;
    private final int position;
    private final int wins;

    public LeaderboardWrapper(String name, EloRating rating, int position) {
        this.name = name;
        this.userId = rating.getUserId();
        this.eloRating = rating.getEloRating();
        this.matches = rating.getMatches();
        this.goals = rating.getGoals();
        this.bulletHits = rating.getBulletHits();
        this.totalScore = rating.getTotalScore();
        this.ballTouches = rating.getBallTouches();
        this.boostTouches = rating.getBoostTouches();
        this.wins = rating.getWins();
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public int getEloRating() {
        return eloRating;
    }

    public int getMatches() {
        return matches;
    }

    public int getGoals() {
        return goals;
    }

    public int getBulletHits() {
        return bulletHits;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getBallTouches() {
        return ballTouches;
    }

    public int getBoostTouches() {
        return boostTouches;
    }

    public int getPosition() {
        return position;
    }

    public int getWins() {
        return wins;
    }

}
