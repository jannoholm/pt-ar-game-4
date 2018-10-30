package com.playtech.ptargame4.server.task.pub.registration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UserUpdaterImpl implements UserUpdater {

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    public UserUpdaterImpl(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    private void init() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                // request from playtech.com
                // create users in local database
            }, 1000, 1023, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    private void pollGetUsers() throws IOException {
/*        HttpURLConnection con = null;
        OutputStream out = null;
        try {
            String urlString = getConfiguration().getManagementServiceUrl();
            URL url = new URL(urlString);
            logger.info("Pushing: " + urlString + " with POST data " + gameStatus.toURLParameters());
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            out = con.getOutputStream();
            out.write(gameStatus.toURLParameters().getBytes("UTF-8"));
            out.close();
            int responseCode = con.getResponseCode();
            logger.info("Result: " + responseCode);
            if ( 200 != responseCode ) {
                throw new IOException( "Invalid response code from server: " + responseCode );
            }
        } finally {
            if (con != null) {
                con.getInputStream().close();
            }
        }*/
    }


}
