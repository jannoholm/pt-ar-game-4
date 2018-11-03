package com.playtech.ptargame4.server.web.task.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtech.ptargame4.server.conf.Configuration;
import com.playtech.ptargame4.server.database.DatabaseAccess;
import com.playtech.ptargame4.server.database.model.User;
import com.playtech.ptargame4.server.util.QrGenerator;
import com.playtech.ptargame4.server.web.model.PollUsersResponse;
import com.playtech.ptargame4.server.web.model.RegisteredUser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserPollerImpl implements UserPoller {

    private static final Logger logger = Logger.getLogger(UserPollerImpl.class.getName());

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;
    private final Configuration configuration;
    private final DatabaseAccess databaseAccess;
    private int latestPosition = 0;

    public UserPollerImpl(ScheduledExecutorService executor, Configuration configuration, DatabaseAccess databaseAccess) {
        this.executor = executor;
        this.configuration = configuration;
        this.databaseAccess = databaseAccess;
    }

    public void init() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                try {
                    // request from playtech.com
                    List<RegisteredUser> result = pollGetUsers();

                    // sort users by position
                    sortUsers(result);

                    // create users in local database
                    updateDatabase(result);
                } catch (Exception e) {
                    logger.log(Level.INFO, "User poller failed.", e);
                }
            }, 1000, 5023, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    private void sortUsers(List<RegisteredUser> result) {
        Collections.sort(result, new Comparator<RegisteredUser>() {
            @Override
            public int compare(RegisteredUser o1, RegisteredUser o2) {
                return o1.getPosition()-o2.getPosition();
            }
        });
    }

    private List<RegisteredUser> pollGetUsers() throws IOException {
        HttpURLConnection con = null;
        try {
            String urlString = configuration.getEndpoints().getPollUsersUrl();
            URL url = new URL(urlString + "/" + latestPosition);
            logger.info("Poll user: " + urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(false);
            int responseCode = con.getResponseCode();
            if ( responseCode == 200 ) {
                List<RegisteredUser> result = readJson(new BufferedInputStream(con.getInputStream()));
                logger.info("Result code: " + responseCode + ", data=" + result);
                return result;
            } else {
                throw new IOException( "Invalid response code from server: " + responseCode );
            }
        } finally {
            if (con != null) {
                con.getInputStream().close();
            }
        }
    }

    private List<RegisteredUser> readJson(InputStream in) throws IOException {
        int pos;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        while ((pos = in.read(buffer, 0, buffer.length)) != -1) {
            result.write(buffer, 0, pos);
        }
        String jsonString = new String(result.toByteArray(), "UTF-8");
        if (jsonString.trim().length() > 0) {
            PollUsersResponse root = new ObjectMapper().readerFor(PollUsersResponse.class).readValue(jsonString);
            return root.getData();
        }
        return Collections.emptyList();
    }

    private void updateDatabase(List<RegisteredUser> result) {
        outer:
        for (RegisteredUser user : result) {
            // ignore invalid data
            if (user.getName() == null || user.getName().trim().length() == 0) continue;
            if (user.getEmail() == null || user.getEmail().trim().length() == 0) continue;

            // check for duplicate
            for (User dbUser : databaseAccess.getUserDatabase().getUsersByName(user.getName().trim())) {
                if (dbUser.getEmail().equalsIgnoreCase(user.getEmail().trim())) {
                    // ignore duplicate
                    updatePosition(user);
                    continue outer;
                }
            }

            // insert new user
            databaseAccess.getUserDatabase().addUser(user.getName(), user.getEmail(), User.UserType.REGULAR, QrGenerator.generateQr());
            updatePosition(user);
        }
    }

    private void updatePosition(RegisteredUser user) {
        latestPosition = latestPosition < user.getPosition() ? user.getPosition() : latestPosition;
    }

}
