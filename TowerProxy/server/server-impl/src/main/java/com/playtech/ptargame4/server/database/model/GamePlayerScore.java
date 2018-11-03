package com.playtech.ptargame4.server.database.model;

import com.playtech.ptargame4.server.registry.GameRegistryGame;

public class GamePlayerScore {

    private final int userId;
    private final GameRegistryGame.Team team;
    private final int towerHealth;
    private final int bridgesBuilt;
    private final int bridgesBuiltPoints;
    private final int bridgesDestroyed;
    private final int bridgesDestroyedPoints;
    private final int bridgeSoldierSaves;
    private final int bridgeSoldierDeaths;
    private final int bridgeSoldierEnemySaves;
    private final int bridgeSoldierEnemyKills;
    private final int score;
    private final int eloRating;
    private final int leaderboardPos;

    public GamePlayerScore(int userId, GameRegistryGame.Team team, int towerHealth, int bridgesBuilt, int bridgesBuiltPoints, int bridgesDestroyed, int bridgesDestroyedPoints, int bridgeSoldierSaves, int bridgeSoldierDeaths, int bridgeSoldierEnemySaves, int bridgeSoldierEnemyKills, int score, int eloRating, int leaderboardPos) {
        this.userId = userId;
        this.team = team;
        this.towerHealth = towerHealth;
        this.bridgesBuilt = bridgesBuilt;
        this.bridgesBuiltPoints = bridgesBuiltPoints;
        this.bridgesDestroyed = bridgesDestroyed;
        this.bridgesDestroyedPoints = bridgesDestroyedPoints;
        this.bridgeSoldierSaves = bridgeSoldierSaves;
        this.bridgeSoldierDeaths = bridgeSoldierDeaths;
        this.bridgeSoldierEnemySaves = bridgeSoldierEnemySaves;
        this.bridgeSoldierEnemyKills = bridgeSoldierEnemyKills;
        this.score = score;
        this.eloRating = eloRating;
        this.leaderboardPos = leaderboardPos;
    }

    public int getUserId() {
        return userId;
    }

    public GameRegistryGame.Team getTeam() {
        return team;
    }

    public int getTowerHealth() {
        return towerHealth;
    }

    public int getBridgesBuilt() {
        return bridgesBuilt;
    }

    public int getBridgesBuiltPoints() {
        return bridgesBuiltPoints;
    }

    public int getBridgesDestroyed() {
        return bridgesDestroyed;
    }

    public int getBridgesDestroyedPoints() {
        return bridgesDestroyedPoints;
    }

    public int getBridgeSoldierSaves() {
        return bridgeSoldierSaves;
    }

    public int getBridgeSoldierDeaths() {
        return bridgeSoldierDeaths;
    }

    public int getBridgeSoldierEnemySaves() {
        return bridgeSoldierEnemySaves;
    }

    public int getBridgeSoldierEnemyKills() {
        return bridgeSoldierEnemyKills;
    }

    public int getScore() {
        return score;
    }

    public int getEloRating() {
        return eloRating;
    }

    public int getLeaderboardPos() {
        return leaderboardPos;
    }
}
