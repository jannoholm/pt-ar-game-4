package com.playtech.ptargame4.server.database;

import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.server.database.model.Occasion;
import com.playtech.ptargame4.server.exception.SystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OccasionDatabaseImpl implements OccasionDatabase {

    private static final Logger logger = Logger.getLogger(UserDatabaseImpl.class.getName());

    private static final String TABLE_OCCASION = "OCCASION";
    private static final String TABLE_CURRENT_OCCASION = "CURRENT_OCCASION";
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final DatabaseAccessImpl dbInit;
    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private volatile Occasion pendingCurrentOccasion;
    private ArrayList<Occasion> pendingWrites = new ArrayList<>();

    private volatile Occasion currentOccasion;
    private Map<Integer, Occasion> occasionMap = new HashMap<>();

    public OccasionDatabaseImpl(DatabaseAccessImpl dbInit, ScheduledExecutorService executor) {
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
            // table users
            String sql =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_OCCASION + " " +
                    "(ID INT PRIMARY KEY     NOT NULL, " +
                    " DESCRIPTION    TEXT    NOT NULL, " +
                    " HIDDEN         INT     NOT NULL)";
            stmt.executeUpdate(sql);

            sql =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_CURRENT_OCCASION + " " +
                    "(ID INT PRIMARY KEY     NOT NULL)";
            stmt.executeUpdate(sql);

            boolean hasRecord = false;
            sql = "select count(1) from " + TABLE_CURRENT_OCCASION;
            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                hasRecord = (resultSet.getInt(1) > 0);
            }

            if (!hasRecord) {
                sql = "insert into " + TABLE_CURRENT_OCCASION + " (ID) values (0)";
                stmt.executeUpdate(sql);
                logger.info("Creating current occasion record.");
            }

            logger.info("Occasions database created!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize occasions database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void readTables() {
        Connection connection = dbInit.allocateConnection();
        try (Statement stmt = connection.createStatement()) {
            String sql = "select ID, DESCRIPTION, HIDDEN from " + TABLE_OCCASION + " order by id";
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("ID");
                String description = result.getString("DESCRIPTION");
                boolean hidden = result.getInt("HIDDEN") > 0;
                Occasion occasion = new Occasion(id, description, hidden);
                occasionMap.put(occasion.getOccasionId(), occasion);
                logger.info("Occasion read from database: " + occasion);
                if (idGenerator.get() < id) {
                    idGenerator.set(id);
                }
            }

            sql = "select ID from " + TABLE_CURRENT_OCCASION;
            result = stmt.executeQuery(sql);
            if (result.next()) {
                int id = result.getInt("ID");
                currentOccasion = occasionMap.get(id);
            }

            logger.info("Occasions read to memory!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize occasions database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void setupMaintenance() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                List<Occasion> todo = new ArrayList<>();
                Occasion newCurrentOccasion;
                synchronized (this) {
                    todo.addAll(pendingWrites);
                    newCurrentOccasion = pendingCurrentOccasion;
                }
                if (todo.size() > 0 || newCurrentOccasion != null) {
                    String selectSql = "select count(1) from " + TABLE_OCCASION + " where ID=?";
                    String insertSql = "insert into " + TABLE_OCCASION + " (ID, DESCRIPTION, HIDDEN) values (?, ?, ?)";
                    String updateSql = "update " + TABLE_OCCASION + " set DESCRIPTION=?, HIDDEN=? where ID=?";
                    String updateCurrentSql = "update " + TABLE_CURRENT_OCCASION + " set ID=?";
                    Connection connection = dbInit.allocateConnection();
                    try (
                            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                            PreparedStatement updateCurrentStmt = connection.prepareStatement(updateCurrentSql)
                    ) {
                        for (Occasion occasion : todo) {
                            selectStmt.setInt(1, occasion.getOccasionId());
                            ResultSet result = selectStmt.executeQuery();
                            result.next();
                            if (result.getInt(1) > 0) {
                                // update
                                updateStmt.setString(1, occasion.getDescription());
                                updateStmt.setInt(2, occasion.isHidden() ? 1 : 0);
                                updateStmt.setInt(3, occasion.getOccasionId());
                                updateStmt.executeUpdate();
                            } else {
                                // insert
                                insertStmt.setInt(1, occasion.getOccasionId());
                                insertStmt.setString(2, occasion.getDescription());
                                insertStmt.setInt(3, occasion.isHidden() ? 1 : 0);
                                insertStmt.executeUpdate();
                            }

                            logger.info("Occasion written to database: " + occasion);
                            synchronized (this) {
                                pendingWrites.remove(occasion);
                            }
                        }

                        if (pendingCurrentOccasion != null) {
                            updateCurrentStmt.setInt(1, newCurrentOccasion.getOccasionId());
                            updateCurrentStmt.executeUpdate();
                            logger.info("Current occasion updated in database to: " + newCurrentOccasion);
                            synchronized (this) {
                                if (newCurrentOccasion == pendingCurrentOccasion) {
                                    pendingCurrentOccasion = null;
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Unable to update occasions database", e);
                        throw new SystemException("Unable to update occasions database", e);
                    } finally {
                        dbInit.releaseConnection(connection);
                    }
                }
            }, 1000, 1094, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    @Override
    public Occasion createOccasion(String description) {
        if (StringUtil.isNull(description)) throw new NullPointerException("Description cannot be null.");

        Occasion newOccasion = new Occasion(idGenerator.incrementAndGet(), description, false);
        synchronized (this) {
            occasionMap.put(newOccasion.getOccasionId(), newOccasion);
            pendingWrites.add(newOccasion);
            if (currentOccasion == null) {
                pendingCurrentOccasion = newOccasion;
                currentOccasion = newOccasion;
            }
        }
        return newOccasion;
    }

    @Override
    public Occasion getOccasion(int occasionId) {
        synchronized (this) {
            return occasionMap.get(occasionId);
        }
    }

    @Override
    public void updateOccasion(Occasion occasion) {
        synchronized (this) {
            occasionMap.put(occasion.getOccasionId(), occasion);
            pendingWrites.add(occasion);
            if (currentOccasion != null && currentOccasion.getOccasionId() == occasion.getOccasionId()) {
                currentOccasion = occasion;
            }
            if (pendingCurrentOccasion != null && pendingCurrentOccasion.getOccasionId() == occasion.getOccasionId()) {
                pendingCurrentOccasion = occasion;
            }
        }
    }

    @Override
    public List<Occasion> listOccasions() {
        synchronized (this) {
            return new ArrayList<>(occasionMap.values());
        }
    }

    @Override
    public Occasion getCurrentOccasion() {
        return currentOccasion;
    }

    @Override
    public void setCurrentOccasion(Occasion occasion) {
        synchronized (this) {
            currentOccasion = occasion;
            pendingCurrentOccasion = occasion;
        }
    }
}
