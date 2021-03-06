package com.playtech.ptargame4.server.database;


import com.playtech.ptargame4.server.database.model.EloRating;
import com.playtech.ptargame4.server.exception.SystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RatingDatabaseImpl implements RatingDatabase {

    private static final Logger logger = Logger.getLogger(RatingDatabaseImpl.class.getName());

    private static final String TABLE_RATING = "ELO_RATING";
    private static final Comparator<EloRating> comparator = new RatingComparator();

    private final DatabaseAccessImpl dbInit;
    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private ArrayList<EloRating> pendingWrites = new ArrayList<>();
    private Map<Integer, EloRating> ratingMap = new HashMap<>();

    public RatingDatabaseImpl(DatabaseAccessImpl dbInit, ScheduledExecutorService executor) {
        this.dbInit = dbInit;
        this.executor = executor;
    }

    protected void init() {
        createTable();
        readTables();
        setupMaintenance();
    }

    private void createTable() {
        Connection connection = dbInit.allocateConnection();
        try (Statement stmt = connection.createStatement()) {
            // table rating
            String sql =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_RATING + " " +
                    "(USERID INT PRIMARY KEY NOT NULL, " +
                    " ELO_RATING            INT     NOT NULL, " +
                    " MATCHES               INT     NOT NULL, " +
                    " TOWER_HEALTH          INT     NOT NULL, " +
                    " ENEMY_TOWER_HEALTH    INT     NOT NULL, " +
                    " TOTAL_SCORE           INT     NOT NULL, " +
                    " WINS                  INT     NOT NULL)";
            stmt.executeUpdate(sql);

            logger.info("Rating database created!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize users database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void readTables() {
        Connection connection = dbInit.allocateConnection();
        try (Statement stmt = connection.createStatement()) {
            String sql = "select USERID, ELO_RATING, MATCHES, TOWER_HEALTH, ENEMY_TOWER_HEALTH, TOTAL_SCORE, WINS from " + TABLE_RATING;
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                int userId = result.getInt("USERID");
                int eloRating = result.getInt("ELO_RATING");
                int matches = result.getInt("MATCHES");
                int towerHealth = result.getInt("TOWER_HEALTH");
                int enemyTowerHealth = result.getInt("ENEMY_TOWER_HEALTH");
                int totalScore = result.getInt("TOTAL_SCORE");
                int wins = result.getInt("WINS");

                EloRating rating = new EloRating(userId, eloRating, matches, towerHealth, enemyTowerHealth, totalScore, wins);
                ratingMap.put(rating.getUserId(), rating);
                logger.info("Rating read from database: " + rating);
            }
            logger.info("Ratings read to memory!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize rating database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void setupMaintenance() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                List<EloRating> todo = new ArrayList<>();
                synchronized (this) {
                    todo.addAll(pendingWrites);
                }
                if (todo.size() > 0) {
                    String selectSql = "select count(1) from " + TABLE_RATING + " where USERID=?";
                    String insertSql = "insert into " + TABLE_RATING + " (USERID, ELO_RATING, MATCHES, TOWER_HEALTH, ENEMY_TOWER_HEALTH, TOTAL_SCORE, WINS) values (?, ?, ?, ?, ?, ?, ?)";
                    String updateSql = "update " + TABLE_RATING + " set ELO_RATING=?, MATCHES=?, TOWER_HEALTH=?, ENEMY_TOWER_HEALTH=?, TOTAL_SCORE=?, WINS=? where USERID=?";
                    Connection connection = dbInit.allocateConnection();
                    try (
                            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                            PreparedStatement updateStmt = connection.prepareStatement(updateSql)
                    ) {
                        for (EloRating rating : todo) {
                            selectStmt.setInt(1, rating.getUserId());
                            ResultSet result = selectStmt.executeQuery();
                            result.next();
                            if (result.getInt(1) > 0) {
                                // update
                                updateStmt.setInt(1, rating.getEloRating());
                                updateStmt.setInt(2, rating.getMatches());
                                updateStmt.setInt(3, rating.getTowerHealth());
                                updateStmt.setInt(4, rating.getEnemyTowerHealth());
                                updateStmt.setInt(5, rating.getTotalScore());
                                updateStmt.setInt(6, rating.getWins());
                                updateStmt.setInt(7, rating.getUserId());
                                updateStmt.executeUpdate();
                            } else {
                                // insert
                                insertStmt.setInt(1, rating.getUserId());
                                insertStmt.setInt(2, rating.getEloRating());
                                insertStmt.setInt(3, rating.getMatches());
                                insertStmt.setInt(4, rating.getTowerHealth());
                                insertStmt.setInt(5, rating.getEnemyTowerHealth());
                                insertStmt.setInt(6, rating.getTotalScore());
                                insertStmt.setInt(7, rating.getWins());
                                insertStmt.executeUpdate();
                            }
                            logger.info("Rating written to database: " + rating);
                            synchronized (this) {
                                pendingWrites.remove(rating);
                            }
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Unable to write to rating database", e);
                        throw new SystemException("Unable to write to rating database", e);
                    } finally {
                        dbInit.releaseConnection(connection);
                    }
                }
            }, 1000, 1053, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    @Override
    public EloRating getRating(int userId) {
        synchronized (this) {
            EloRating rating = ratingMap.get(userId);
            if (userId == 0 || rating == null) {
                rating = new EloRating(userId, 1000, 0, 0, 0, 0, 0);
            }
            return rating;
        }
    }

    @Override
    public void updateRating(EloRating rating) {
        synchronized (this) {
            if (rating.getUserId() <= 0) {
                logger.info("Ignoring anonymous rating: " + rating);
                return;
            } else if (ratingMap.containsKey(rating.getUserId())) {
                logger.info("Adding update pending writes: " + rating);
            } else {
                logger.info("Adding insert pending writes: " + rating);
            }
            ratingMap.put(rating.getUserId(), rating);
            pendingWrites.add(rating);
        }
    }

    @Override
    public Collection<EloRating> getLeaderboard() {
        synchronized (this) {
            ArrayList<EloRating> leaderboard = new ArrayList<>();
            for (EloRating rating : ratingMap.values()) {
                leaderboard.add(rating);
            }
            Collections.sort(leaderboard, comparator);
            return leaderboard;
        }
    }

    private static final class RatingComparator implements Comparator<EloRating> {

        @Override
        public int compare(EloRating o1, EloRating o2) {
            return o2.getEloRating() - o1.getEloRating();
        }
    }

}
