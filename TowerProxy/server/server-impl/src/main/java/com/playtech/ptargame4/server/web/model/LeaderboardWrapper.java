package com.playtech.ptargame4.server.web.model;

import com.playtech.ptargame4.server.database.model.EloRating;

public class LeaderboardWrapper {
    private final String name;
    private final int userId;
    private final int eloRating;
    private final int matches;
    private final int towerHealth;
    private final int enemyTowerHealth;
    private final int totalScore;
    private final int position;
    private final int wins;

    public LeaderboardWrapper(String name, EloRating rating, int position) {
        this.name = name;
        this.userId = rating.getUserId();
        this.eloRating = rating.getEloRating();
        this.matches = rating.getMatches();
        this.towerHealth = rating.getTowerHealth();
        this.enemyTowerHealth = rating.getEnemyTowerHealth();
        this.totalScore = rating.getTotalScore();
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

    public int getTowerHealth() {
        return towerHealth;
    }

    public int getEnemyTowerHealth() {
        return enemyTowerHealth;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getPosition() {
        return position;
    }

    public int getWins() {
        return wins;
    }

}
