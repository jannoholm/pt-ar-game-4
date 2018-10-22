package com.playtech.ptargame4.server.database;

import com.playtech.ptargame4.server.database.model.ActionToken;
import com.playtech.ptargame4.server.exception.SystemException;
import com.playtech.ptargame4.server.registry.GameRegistryGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ActionTokenDatabaseImpl implements ActionTokenDatabase {

    private static final Logger logger = Logger.getLogger(GameDatabaseImpl.class.getName());

    private static final String CONF_LOCATON = "conf/token.properties";

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private long lastUpdateTime = 0;
    private volatile Map<String, ActionToken> dataMap = Collections.emptyMap();

    public ActionTokenDatabaseImpl(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    protected void init() {
        updateConf();
        setupMaintenance();
    }

    private void updateConf() {
        File confFile = new File(CONF_LOCATON);

        long lastModified = confFile.lastModified();
        if ( lastUpdateTime < lastModified ) {
            try {
                Map<String, ActionToken> newConf = new HashMap<>();

                Properties properties = new Properties();
                try (InputStream in = new FileInputStream(confFile)) {
                    properties.load(in);
                }
                for (int i = 0; ;++i) {
                    String indexStr = properties.getProperty("token." + i + ".index");
                    if (indexStr == null) break;

                    int index = Integer.parseInt(indexStr);
                    GameRegistryGame.Team team = GameRegistryGame.Team.valueOf(properties.getProperty("token." + i + ".team"));
                    ActionToken.TokenType tokenType = ActionToken.TokenType.getTokenType(properties.getProperty("token." + i + ".type"));
                    ActionToken actionToken = new ActionToken(String.valueOf(i), tokenType, (byte)index, team);
                    newConf.put(actionToken.getQrCode(), actionToken);
                }

                dataMap = newConf;
                lastUpdateTime = lastModified;
            } catch (Exception e) {
                throw new SystemException("Unable to initialize token conf", e);
            }
        }
    }

    private void setupMaintenance() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                updateConf();
            }, 1000, 1082, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    @Override
    public ActionToken getActionToken(String qrCode) {
        return dataMap.get(qrCode);
    }
}
