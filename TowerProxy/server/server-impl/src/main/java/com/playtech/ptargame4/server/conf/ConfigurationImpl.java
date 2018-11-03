package com.playtech.ptargame4.server.conf;

import com.playtech.ptargame4.server.conf.model.ActionToken;
import com.playtech.ptargame4.server.conf.model.EndPoints;
import com.playtech.ptargame4.server.exception.SystemException;
import com.playtech.ptargame4.server.registry.GameRegistryGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ConfigurationImpl implements Configuration {

    private static final Logger logger = Logger.getLogger(ConfigurationImpl.class.getName());

    private static final String CONF_FILE = "conf/application.properties";
    private static final String PROP_NAME_WEB_PORT = "port.web";
    private static final String PROP_NAME_BINARY_PORT = "port.binary";
    private static final String PROP_NAME_POLLUSERS_URL = "pub.pollUsers.url";
    private static final String PROP_NAME_PUSHDASH_URL = "pub.pushDash.url";
    private static final int PROP_VALUE_WEB_PORT = 8100;
    private static final int PROP_VALUE_BINARY_PORT = 8101;
    private static final String PROP_VALUE_POLLUSERS = "http://localhost:8100/mock/pollUsers";
    private static final String PROP_VALUE_PUSHDASH = "http://localhost:8100/mock/pushDash";

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private long lastUpdateTime = -1;
    private int webPort = PROP_VALUE_WEB_PORT;
    private int binaryPort = PROP_VALUE_BINARY_PORT;
    private volatile EndPoints endPoints = new EndPoints(PROP_VALUE_POLLUSERS, PROP_VALUE_PUSHDASH);
    private volatile Map<String, ActionToken> actionTokens;

    public ConfigurationImpl(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void init() {
        updateConf();
        setupMaintenance();
    }

    private void updateConf() {
        File confFile = new File(CONF_FILE);

        // update if file has changed
        long lastModified = confFile.exists() ? confFile.lastModified() : 0;
        if ( lastUpdateTime < lastModified ) {
            try {
                Properties newConf = new Properties();
                if (confFile.exists()) {
                    try (InputStream in = new FileInputStream(confFile)) {
                        newConf.load(in);
                    }
                }

                // read conf
                this.webPort = readIntProperty(newConf, PROP_NAME_WEB_PORT, PROP_VALUE_WEB_PORT);
                this.binaryPort = readIntProperty(newConf, PROP_NAME_BINARY_PORT, PROP_VALUE_BINARY_PORT);
                this.endPoints = readEndpoints(newConf);
                this.actionTokens = readActionTokens(newConf);
                logger.info("Configuration.endpoints: " + this.endPoints);
                logger.info("Configuration.actionTokens: " + this.actionTokens);
                logger.info("Configuration.webport: " + this.webPort);
                logger.info("Configuration.binaryport: " + this.binaryPort);

                lastUpdateTime = lastModified;
            } catch (Exception e) {
                throw new SystemException("Unable to initialize token conf", e);
            }
        }
    }

    private int readIntProperty(Properties newConf, String name, int defaultValue) {
        String prop = newConf.getProperty(name);
        if (prop != null) {
            try {
                return Integer.parseInt(prop);
            } catch (Exception e) {
                logger.info("Unable to read property: " + name);
            }
        }
        return defaultValue;
    }

    private Map<String, ActionToken> readActionTokens(Properties newConf) {
        Map<String, ActionToken> newTokens = new HashMap<>();
        loop:
        for (int i = 0; ;++i) {
            String indexStr = newConf.getProperty("token." + i + ".index");
            ActionToken actionToken;
            if (indexStr == null) {
                switch (i) {
                    case 0:
                        actionToken = new ActionToken(String.valueOf(i), ActionToken.TokenType.BRIDGE, (byte)0, GameRegistryGame.Team.RED);
                        break;
                    case 1:
                        actionToken = new ActionToken(String.valueOf(i), ActionToken.TokenType.BRIDGE, (byte)1, GameRegistryGame.Team.RED);
                        break;
                    case 2:
                        actionToken = new ActionToken(String.valueOf(i), ActionToken.TokenType.BRIDGE, (byte)0, GameRegistryGame.Team.BLUE);
                        break;
                    case 3:
                        actionToken = new ActionToken(String.valueOf(i), ActionToken.TokenType.BRIDGE, (byte)1, GameRegistryGame.Team.BLUE);
                        break;
                    default:
                        break loop;
                }
            } else {
                int index = Integer.parseInt(indexStr);
                GameRegistryGame.Team team = GameRegistryGame.Team.valueOf(newConf.getProperty("token." + i + ".team"));
                ActionToken.TokenType tokenType = ActionToken.TokenType.getTokenType(newConf.getProperty("token." + i + ".type"));
                actionToken = new ActionToken(String.valueOf(i), tokenType, (byte)index, team);
            }

            newTokens.put(actionToken.getQrCode(), actionToken);
        }
        return newTokens;
    }

    private EndPoints readEndpoints(Properties newConf) {
        return new EndPoints(
                newConf.getProperty(PROP_NAME_POLLUSERS_URL, PROP_VALUE_POLLUSERS),
                newConf.getProperty(PROP_NAME_PUSHDASH_URL, PROP_VALUE_PUSHDASH)
        );
    }

    private void setupMaintenance() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(
                    this::updateConf,
                    1000,
                    1095,
                    TimeUnit.MILLISECONDS
            );
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }



    @Override
    public EndPoints getEndpoints() {
        return endPoints;
    }

    @Override
    public ActionToken getActionToken(String qrCode) {
        return actionTokens.get(qrCode);
    }

    @Override
    public int getWebPort() {
        return webPort;
    }

    @Override
    public int getBinaryPort() {
        return binaryPort;
    }
}
