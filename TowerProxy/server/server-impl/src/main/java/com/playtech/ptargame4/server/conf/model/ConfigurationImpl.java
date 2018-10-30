package com.playtech.ptargame4.server.conf.model;

import com.playtech.ptargame4.server.exception.SystemException;

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

public class ConfigurationImpl extends Configuration {

    private static final Logger logger = Logger.getLogger(ConfigurationImpl.class.getName());

    private static final String CONF_LOCATON = "conf/application.properties";

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private long lastUpdateTime = 0;
    private volatile Properties dataMap = new Properties();

    public ConfigurationImpl(ScheduledExecutorService executor) {
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
                Properties newConf = new Properties();
                try (InputStream in = new FileInputStream(confFile)) {
                    newConf.load(in);
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
            }, 1000, 1095, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }
}
