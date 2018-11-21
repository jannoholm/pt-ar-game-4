package com.playtech.ptargame4.server.web.task.dashboard;

import com.playtech.ptargame4.server.conf.Configuration;
import com.playtech.ptargame4.server.database.DatabaseAccess;
import com.playtech.ptargame4.server.web.model.LeaderboardWrapper;
import com.playtech.ptargame4.server.web.model.ResponseWrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaderboardPusherImpl implements LeaderboardPusher {

    private static final Logger logger = Logger.getLogger(LeaderboardPusherImpl.class.getName());

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;
    private final Configuration configuration;
    private final DatabaseAccess databaseAccess;

    public LeaderboardPusherImpl(ScheduledExecutorService executor, Configuration configuration, DatabaseAccess databaseAccess) {
        this.executor = executor;
        this.configuration = configuration;
        this.databaseAccess = databaseAccess;
    }

    public void init() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                try {
                    pushLeaderboard(ListLeaderBoardTask.execute(databaseAccess));
                } catch (Exception e) {
                    logger.log(Level.INFO, "Leaderboard pusher failed.", e);
                }
            }, 1000, 10028, TimeUnit.MILLISECONDS);
            logger.info("Leaderboard pusher set up with 10s interval.");
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    private void pushLeaderboard(Collection<LeaderboardWrapper> leaderboard) throws IOException {
        HttpURLConnection con = null;
        try {
            String urlString = configuration.getEndpoints().getPushLeaderboardUrl();
            if (urlString.equalsIgnoreCase("disable")) {
                return;
            }
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();
            out.write("data=".getBytes());
            out.write(new ResponseWrapper(leaderboard).toBytes());
            out.close();
            int responseCode = con.getResponseCode();
            if ( responseCode == 200 ) {
                logger.fine("Leaderboard update pushed.");
            } else {
                throw new IOException( "Unable to push leaderboard: " + responseCode );
            }
        } finally {
            if (con != null) {
                con.getInputStream().close();
            }
        }
    }

}
