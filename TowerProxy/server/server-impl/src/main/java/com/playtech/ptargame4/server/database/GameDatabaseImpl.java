package com.playtech.ptargame4.server.database;


import com.playtech.ptargame4.server.database.model.Game;
import com.playtech.ptargame4.server.database.model.GamePlayerScore;
import com.playtech.ptargame4.server.exception.SystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameDatabaseImpl implements GameDatabase {

    private static final Logger logger = Logger.getLogger(GameDatabaseImpl.class.getName());

    private static final String TABLE_GAMES = "GAMES";
    private static final String TABLE_SCORE = "GAMES_PLAYER_SCORE";

    private final DatabaseAccessImpl dbInit;
    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private ArrayList<Game> pendingWrites = new ArrayList<>();

    public GameDatabaseImpl(DatabaseAccessImpl dbInit, ScheduledExecutorService executor) {
        this.dbInit = dbInit;
        this.executor = executor;
    }

    protected void init() {
        createTable();
        setupMaintenance();
    }

    protected void createTable() {
        Connection connection = dbInit.allocateConnection();
        try (Statement stmt = connection.createStatement()) {

            // table games
            String sql =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_GAMES + " " +
                    "(GAMEID                TEXT    PRIMARY KEY NOT NULL, " +
                    " EVENT                 INT     NOT NULL, " +
                    " TOWER_HEALTH_RED      INT     NOT NULL, " +
                    " TOWER_HEALTH_BLUE     INT     NOT NULL, " +
                    " GAME_TIME             INT     NOT NULL)";
            stmt.executeUpdate(sql);

            // table player score
            sql =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_SCORE + " " +
                    "(GAMEID                        TEXT    NOT NULL, " +
                    " USERID                        INT     NOT NULL, " +
                    " TEAM                          INT     NOT NULL, " +
                    " TOWER_HEALTH                  INT     NOT NULL, " +
                    " BRIDGES_BUILT                 INT     NOT NULL, " +
                    " BRIDGES_BUILT_POINTS          INT     NOT NULL, " +
                    " BRIDGES_DESTROYED             INT     NOT NULL, " +
                    " BRIDGES_DESTROYED_POINTS      INT     NOT NULL, " +
                    " BRIDGE_SOLDIER_SAVES          INT     NOT NULL, " +
                    " BRIDGE_SOLDIER_DEATHS         INT     NOT NULL, " +
                    " BRIDGE_SOLDIER_ENEMY_SAVES    INT     NOT NULL, " +
                    " BRIDGE_SOLDIER_ENEMY_KILLS    INT     NOT NULL, " +
                    " SCORE                         INT     NOT NULL, " +
                    " ELO_RAITING                   INT     NOT NULL, " +
                    " LEADERBOARD_POS               INT     NOT NULL)";
            stmt.executeUpdate(sql);

            logger.info("Games database created!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize games database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void setupMaintenance() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                List<Game> todo = new ArrayList<>();
                synchronized (this) {
                    todo.addAll(pendingWrites);
                }
                if (todo.size() > 0) {
                    String insertGameSql = "insert into " + TABLE_GAMES + " (GAMEID, EVENT, TOWER_HEALTH_RED, TOWER_HEALTH_BLUE, GAME_TIME) values (?, ?, ?, ?, ?)";
                    String insertScoreSql = "insert into " + TABLE_SCORE + " (GAMEID, USERID, TEAM, TOWER_HEALTH, BRIDGES_BUILT, BRIDGES_BUILT_POINTS, BRIDGES_DESTROYED, BRIDGES_DESTROYED_POINTS, BRIDGE_SOLDIER_SAVES, BRIDGE_SOLDIER_DEATHS, BRIDGE_SOLDIER_ENEMY_SAVES, BRIDGE_SOLDIER_ENEMY_KILLS, SCORE, ELO_RAITING, LEADERBOARD_POS) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    Connection connection = dbInit.allocateConnection();
                    try (
                            PreparedStatement insertGameStmt = connection.prepareStatement(insertGameSql);
                            PreparedStatement insertScoreStmt = connection.prepareStatement(insertScoreSql);
                    ) {
                        for (Game game : todo) {
                            insertGameStmt.setString(1, game.getGameId());
                            insertGameStmt.setInt(2, game.getEventId());
                            insertGameStmt.setInt(3, game.getTowerHealthRed());
                            insertGameStmt.setInt(4, game.getTowerHealthBlue());
                            insertGameStmt.setInt(5, game.getGameTime());
                            insertGameStmt.executeUpdate();

                            for (GamePlayerScore score : game.getPlayerScores()) {
                                insertScoreStmt.setString(1, game.getGameId());
                                insertScoreStmt.setInt(2, score.getUserId());
                                insertScoreStmt.setInt(3, score.getTeam().ordinal());
                                insertScoreStmt.setInt(4, score.getTowerHealth());
                                insertScoreStmt.setInt(5, score.getBridgesBuilt());
                                insertScoreStmt.setInt(6, score.getBridgesBuiltPoints());
                                insertScoreStmt.setInt(7, score.getBridgesDestroyed());
                                insertScoreStmt.setInt(8, score.getBridgesDestroyedPoints());
                                insertScoreStmt.setInt(9, score.getBridgeSoldierSaves());
                                insertScoreStmt.setInt(10, score.getBridgeSoldierDeaths());
                                insertScoreStmt.setInt(11, score.getBridgeSoldierEnemySaves());
                                insertScoreStmt.setInt(12, score.getBridgeSoldierEnemyKills());
                                insertScoreStmt.setInt(13, score.getScore());
                                insertScoreStmt.setInt(14, score.getEloRating());
                                insertScoreStmt.setInt(15, score.getLeaderboardPos());
                                insertScoreStmt.addBatch();
                            }
                            insertScoreStmt.executeBatch();
                            logger.info("Game written to database: " + game);
                            synchronized (this) {
                                pendingWrites.remove(game);
                            }
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Unable to write to games database", e);
                        throw new SystemException("Unable to write to games database", e);
                    } finally {
                        dbInit.releaseConnection(connection);
                    }
                }
            }, 1000, 1082, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    @Override
    public void addGame(Game game) {
        synchronized (this) {
            this.pendingWrites.add(game);
        }
    }
}
