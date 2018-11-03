package com.playtech.ptargame4.server.database.model;


import java.util.Collection;
import java.util.Collections;

// TODO: Update to Tower Attack 
public class Game {
    private final String gameId;
    private final int eventId;
    private final int towerHealthRed;
    private final int towerHealthBlue;
    private final int gameTime;
    private final Collection<GamePlayerScore> playerScores;

    public Game(
            String gameId, int eventId, int towerHealthRed, int towerHealthBlue,
            int gameTime, Collection<GamePlayerScore> playerScores) {
        this.gameId = gameId;
        this.eventId = eventId;
        this.towerHealthRed = towerHealthRed;
        this.towerHealthBlue = towerHealthBlue;
        this.gameTime = gameTime;
        this.playerScores = Collections.unmodifiableCollection(playerScores);
    }

    public String getGameId() {
        return gameId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getTowerHealthRed() {
        return towerHealthRed;
    }

    public int getTowerHealthBlue() {
        return towerHealthBlue;
    }

    public int getGameTime() {
        return gameTime;
    }

    public Collection<GamePlayerScore> getPlayerScores() {
        return playerScores;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", eventId=" + eventId +
                ", towerHealthRed=" + towerHealthRed +
                ", towerHealthBlue=" + towerHealthBlue +
                ", gameTime=" + gameTime +
                ", playerScores=" + playerScores +
                '}';
    }
}
