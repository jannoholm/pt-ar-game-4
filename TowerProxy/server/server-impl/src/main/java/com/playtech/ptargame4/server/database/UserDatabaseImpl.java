package com.playtech.ptargame4.server.database;


import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.server.database.model.User;
import com.playtech.ptargame4.server.exception.SystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDatabaseImpl implements UserDatabase {

    private static final Logger logger = Logger.getLogger(UserDatabaseImpl.class.getName());

    private static final String TABLE_USERS = "USERS";
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final DatabaseAccessImpl dbInit;
    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private ArrayList<User> pendingWrites = new ArrayList<>();
    private Map<Integer, User> userMap = new HashMap<>();

    public UserDatabaseImpl(DatabaseAccessImpl dbInit, ScheduledExecutorService executor) {
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
                    "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " " +
                    "(ID INT PRIMARY KEY     NOT NULL, " +
                    " NAME           TEXT    NOT NULL, " +
                    " EMAIL          TEXT    NOT NULL, " +
                    " HIDDEN         INT     NOT NULL DEFAULT 0, " +
                    " INTERNAL       INT     NOT NULL DEFAULT 0, " +
					" INFORMATION    TEXT    , " +
                    " QR_CODE        TEXT    NOT NULL)";
            stmt.executeUpdate(sql);

            logger.info("Users database created!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize users database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void readTables() {
        Connection connection = dbInit.allocateConnection();
        try (Statement stmt = connection.createStatement()) {
            String sql = "select ID, NAME, EMAIL, HIDDEN, INTERNAL, INFORMATION, QR_CODE from " + TABLE_USERS + " order by id";
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("ID");
                String name = result.getString("NAME");
                String email = result.getString("EMAIL");
                int hidden = result.getInt("HIDDEN");
                int internal = result.getInt("INTERNAL");
                String qrCode = result.getString("QR_CODE");
				String information = result.getString("INFORMATION");
                User user = new User(id, name.toUpperCase(), email, hidden > 0, User.UserType.getUserType(internal), qrCode, information);
                userMap.put(user.getId(), user);
                logger.info("User read from database: " + user);
                if (idGenerator.get() < id) {
                    idGenerator.set(id);
                }
            }
            logger.info("Users read to memory!");
        } catch (Exception e) {
            throw new SystemException("Unable to initialize users database", e);
        } finally {
            dbInit.releaseConnection(connection);
        }
    }

    private void setupMaintenance() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                List<User> todo = new ArrayList<>();
                synchronized (this) {
                    todo.addAll(pendingWrites);
                }
                if (todo.size() > 0) {
                    String selectSql = "select count(1) from " + TABLE_USERS + " where ID=?";
                    String insertSql = "insert into " + TABLE_USERS + " (ID, NAME, EMAIL, HIDDEN, INTERNAL, QR_CODE, INFORMATION) values (?, ?, ?, ?, ?, ?, ?)";
                    String updateSql = "update " + TABLE_USERS + " set NAME=?, EMAIL=?, HIDDEN=?, INTERNAL=?, QR_CODE=?, INFORMATION=? where ID=?";
                    Connection connection = dbInit.allocateConnection();
                    try (
                            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                    ) {
                        for (User user : todo) {
                            selectStmt.setInt(1, user.getId());
                            ResultSet result = selectStmt.executeQuery();
                            result.next();
                            if (result.getInt(1) > 0) {
                                // update
                                updateStmt.setString(1, user.getName());
                                updateStmt.setString(2, user.getEmail());
                                updateStmt.setInt(3, user.isHidden() ? 1 : 0);
                                updateStmt.setInt(4, user.getUserType().ordinal());
                                updateStmt.setString(5, user.getQrCode());
								updateStmt.setString(6, user.getInformation());
                                updateStmt.setInt(7, user.getId());
                                updateStmt.executeUpdate();
                            } else {
                                // insert
                                insertStmt.setInt(1, user.getId());
                                insertStmt.setString(2, user.getName());
                                insertStmt.setString(3, user.getEmail());
                                insertStmt.setInt(4, user.isHidden() ? 1 : 0);
                                insertStmt.setInt(5, user.getUserType().ordinal());
                                insertStmt.setString(6, user.getQrCode());
								insertStmt.setString(7, user.getInformation());
                                insertStmt.executeUpdate();
                            }
                            logger.info("User written to database: " + user);
                            synchronized (this) {
                                pendingWrites.remove(user);
                            }
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Unable to initialize users database", e);
                        throw new SystemException("Unable to initialize users database", e);
                    } finally {
                        dbInit.releaseConnection(connection);
                    }
                }
            }, 1000, 1075, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    public User addUser(String name, String email, User.UserType userType, String qrCode, String information) {
        if (StringUtil.isNull(name)) throw new NullPointerException("Name cannot be null.");
        if (StringUtil.isNull(email)) throw new NullPointerException("Email cannot be null.");
        if (StringUtil.isNull(qrCode)) throw new NullPointerException("qrCode cannot be null.");
        User user = new User(idGenerator.incrementAndGet(), name.toUpperCase().trim(), email.toUpperCase().trim(), false, userType, qrCode, information);
        synchronized (this) {
            pendingWrites.add(user);
            userMap.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User getUser(int id) {
        synchronized (this) {
            return userMap.get(id);
        }
    }

    @Override
    public User getUser(String qrCode) {
        ArrayList<User> match = new ArrayList<>();
        for (User user : getUsers()) {
            if (user.getQrCode().equals(qrCode)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        if (StringUtil.isNull(user.getName())) throw new NullPointerException("Name cannot be null.");
        if (StringUtil.isNull(user.getQrCode())) throw new NullPointerException("QrCode cannot be null.");
        synchronized (this) {
            User existing = userMap.get(user.getId());
            if (existing != null) {
                user = new User(user.getId(), user.getName().toUpperCase(), user.getEmail(), user.isHidden(), user.getUserType(), user.getQrCode(), user.getInformation());
                userMap.put(user.getId(), user);
                pendingWrites.add(user);
                logger.info("Adding to pending writes");
            } else {
                logger.info("player not found: " + user);
            }
        }
    }

    public Collection<User> getUsers() {
        synchronized (this) {
            return Collections.unmodifiableCollection(new ArrayList<>(userMap.values()));
        }
    }

    @Override
    public Collection<User> getUsers(String filter) {
        Collection<User> users = getUsers();
        if (StringUtil.isNull(filter)) {
            return users;
        }

        filter = filter.toUpperCase();
        ArrayList<User> match = new ArrayList<>();
        for (User user : users) {
            if (user.getName().contains(filter)) {
                match.add(user);
            }
        }
        return Collections.unmodifiableCollection(match);
    }

    @Override
    public Collection<User> getUsersByName(String name) {
        if (StringUtil.isNull(name)) throw new NullPointerException("Name cannot be null.");
        name = name.toUpperCase();
        ArrayList<User> match = new ArrayList<>();
        for (User user : getUsers()) {
            if (user.getName().equals(name)) {
                match.add(user);
            }
        }
        return Collections.unmodifiableCollection(match);
    }

}
