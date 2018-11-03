package com.playtech.ptargame4.server.database.model;


public class EloRating {

    private final int userId;
    private final int eloRating;
    private final int matches;
    private final int towerHealth;
    private final int enemyTowerHealth;
    private final int totalScore;
    private final int wins;

    public EloRating(int userId, int eloRating, int matches, int towerHealth, int enemyTowerHealth, int totalScore, int wins) {
        this.userId = userId;
        this.eloRating = eloRating;
        this.matches = matches;
        this.towerHealth = towerHealth;
        this.enemyTowerHealth = enemyTowerHealth;
        this.totalScore = totalScore;
        this.wins = wins;
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

    public int getWins() {
        return wins;
    }
}
