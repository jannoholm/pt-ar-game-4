package com.playtech.ptargame4.server.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.playtech.ptargame.common.callback.ClientRegistry;
import com.playtech.ptargame.common.message.MessageParser;
import com.playtech.ptargame.common.session.Session;
import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.api.lobby.Team;
import com.playtech.ptargame4.api.table.SetUserOnMapRequest;
import com.playtech.ptargame4.api.token.TokenLocationUpdateMessage;
import com.playtech.ptargame4.server.conf.Configuration;
import com.playtech.ptargame4.server.database.DatabaseAccess;
import com.playtech.ptargame4.server.conf.model.ActionToken;
import com.playtech.ptargame4.server.database.model.EloRating;
import com.playtech.ptargame4.server.database.model.Occasion;
import com.playtech.ptargame4.server.database.model.User;
import com.playtech.ptargame4.server.exception.SystemException;
import com.playtech.ptargame4.server.util.ActionTokenTypeConverter;
import com.playtech.ptargame4.server.util.QrGenerator;
import com.playtech.ptargame4.server.util.TeamConverter;
import com.playtech.ptargame4.server.web.model.OccasionWrapper;
import com.playtech.ptargame4.server.web.model.RegisteredUser;
import com.playtech.ptargame4.server.web.model.ResponseWrapper;
import com.playtech.ptargame4.server.web.model.UserWrapper;
import com.playtech.ptargame4.server.web.task.dashboard.ListLeaderBoardTask;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.xml.ws.http.HTTPException;

/**
 * Simple http server from core java.
 *
 * @author Janno
 */
public final class WebListener {
    private static final Logger logger = Logger.getLogger(WebListener.class.getName());
    private static final String CTX_LEADERBOARD = "/leaderboard";
    private static final String CTX_USER = "/player";
    private static final String CTX_OCCASION = "/occasion";
    private static final String CTX_CURRENT_OCCASION = "/current_occasion";
    private static final String CTX_HTML = "/html";
    private static final String CTX_SERVER = "/server";
    private static final String CTX_CONTROL_USER = "/control/user";
    private static final String CTX_CONTROL_POSITION = "/control/position";
    private static final String CTX_MOCK_POLLUSERS = "/mock/pollUsers";
    private static final String CTX_MOCK_PUSHDASH = "/mock/pushLeaderboard";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";
    private static final String ENCODING = "UTF-8";

    private static final String HTML_DIR = "html";

    private final HttpServer s;
    private final DatabaseAccess databaseAccess;
    private final ClientRegistry clientRegistry;
    private final MessageParser messageParser;
    private final Configuration configuration;

    public WebListener(DatabaseAccess databaseAccess, ClientRegistry clientRegistry, MessageParser messageParser, Configuration configuration) throws IOException {
        this.databaseAccess = databaseAccess;
        this.clientRegistry = clientRegistry;
        this.messageParser = messageParser;
        this.configuration = configuration;
        s = HttpServer.create(new InetSocketAddress(getPort()), 0);
    }

    private int getPort() {
        return this.configuration.getWebPort();
    }

    public void start() {
        HttpContext ctxServer = s.createContext(CTX_SERVER);
        ctxServer.setHandler(this::handleExchange);

        HttpContext ctxLeaderboard = s.createContext(CTX_LEADERBOARD);
        ctxLeaderboard.setHandler(this::handleExchange);

        HttpContext ctxCompetitor = s.createContext(CTX_USER);
        ctxCompetitor.setHandler(this::handleExchange);

        HttpContext ctxControlUser = s.createContext(CTX_CONTROL_USER);
        ctxControlUser.setHandler(this::handleExchange);

        HttpContext ctxControlOccasion = s.createContext(CTX_OCCASION);
        ctxControlOccasion.setHandler(this::handleExchange);

        HttpContext ctxControlCurrentOccasion = s.createContext(CTX_CURRENT_OCCASION);
        ctxControlCurrentOccasion.setHandler(this::handleExchange);

        HttpContext ctxControlPosition = s.createContext(CTX_CONTROL_POSITION);
        ctxControlPosition.setHandler(this::handleExchange);

        HttpContext ctxHtml = s.createContext(CTX_HTML);
        ctxHtml.setHandler(this::handleExchange);

        HttpContext ctxPollUsers = s.createContext(CTX_MOCK_POLLUSERS);
        ctxPollUsers.setHandler(this::handleExchange);

        HttpContext ctxPushDash = s.createContext(CTX_MOCK_PUSHDASH);
        ctxPushDash.setHandler(this::handleExchange);

        if (s.getExecutor()!=null)
            throw new IllegalStateException();

        s.setExecutor(createExecutor());
        s.start();

        logger.info("Started HttpUtilityServer on port " + s.getAddress().getPort());
    }

    private void handleExchange(HttpExchange httpExchange) throws IOException {
        try {
            URI uri = relative(httpExchange);
            String path = uri.getPath();

            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("HttpUtilityServer Serving uri: %s, from %s", httpExchange.getRequestURI(), httpExchange.getRemoteAddress()));
            }

            switch(httpExchange.getHttpContext().getPath()){
                case CTX_SERVER:
                    processServer(httpExchange, path);
                    break;
                case CTX_LEADERBOARD:
                    processLeaderboard(httpExchange, path);
                    break;
                case CTX_USER:
                    processUser(httpExchange, path);
                    break;
                case CTX_OCCASION:
                    processOccasion(httpExchange, path);
                    break;
                case CTX_CURRENT_OCCASION:
                    processCurrentOccasion(httpExchange, path);
                    break;
                case CTX_CONTROL_POSITION:
                    processControlPosition(httpExchange, path);
                    break;
                case CTX_CONTROL_USER:
                    processControlUser(httpExchange, path);
                    break;
                case CTX_HTML:
                    processHtml(httpExchange, path);
                    break;
                case CTX_MOCK_POLLUSERS:
                    processMockUsers(httpExchange, path);
                    break;
                case CTX_MOCK_PUSHDASH:
                    processLeaderboardPush(httpExchange, path);
                    break;
            }

        }catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Unable to handle request: "+httpExchange, e);

            byte[] b = e.getMessage().getBytes();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, b.length);
            try(OutputStream out= httpExchange.getResponseBody()){
                out.write(b);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void processServer(HttpExchange httpExchange, String path) throws IOException {
        switch(path){
            case "status":
                checkRequestMethod(httpExchange, METHOD_GET);
                getStatusRequest(httpExchange);
                break;
            default:
                if (logger.isLoggable(Level.INFO)) {
                    logger.info(String.format("HttpUtilityServer Cannot serve uri: %s, invalid path %s", httpExchange.getRequestURI(), path));
                }
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0L);
                break;
        }
    }

    private void processLeaderboard(HttpExchange httpExchange, String path) throws IOException {
        if (METHOD_GET.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
            // get players
            listLeaderboard(httpExchange);
        } else if (METHOD_GET.equals(httpExchange.getRequestMethod())) {
            // get player
            getUserRating(httpExchange, path);
        }
    }

    private void processUser(HttpExchange httpExchange, String path) throws IOException {
        if (METHOD_POST.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
            // create player
            createUserRequest(httpExchange);
        } else if (METHOD_POST.equals(httpExchange.getRequestMethod())) {
            // update player
            updateUserRequest(httpExchange, path);
        } else if (METHOD_DELETE.equals(httpExchange.getRequestMethod())) {
            // delete player
            deleteUserRequest(httpExchange, path);
        } else if (METHOD_GET.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
            // get players
            listUsersRequest(httpExchange);
        } else if (METHOD_GET.equals(httpExchange.getRequestMethod())) {
            // get player
            getUserRequest(httpExchange, path);
        }
    }

    private void processOccasion(HttpExchange httpExchange, String path) throws IOException {
        if (METHOD_POST.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
            // create player
            createOccasionRequest(httpExchange);
        } else if (METHOD_POST.equals(httpExchange.getRequestMethod())) {
            // update player
            updateOccasionRequest(httpExchange, path);
        } else if (METHOD_DELETE.equals(httpExchange.getRequestMethod())) {
            // delete player
            deleteOccasionRequest(httpExchange, path);
        } else if (METHOD_GET.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
            // get players
            listOccasionsRequest(httpExchange);
        } else if (METHOD_GET.equals(httpExchange.getRequestMethod())) {
            // get player
            getOccasionRequest(httpExchange, path);
        }
    }

    private void processCurrentOccasion(HttpExchange httpExchange, String path) throws IOException {
        if (METHOD_POST.equals(httpExchange.getRequestMethod()) && path.trim().length() > 0) {
            // update player
            updateCurrentOccasionRequest(httpExchange, path);
        } else if (METHOD_GET.equals(httpExchange.getRequestMethod())) {
            // get player
            getCurrentOccasionRequest(httpExchange, path);
        }
    }

    private void processControlUser(HttpExchange httpExchange, String path) throws IOException {
        try {
            if (METHOD_POST.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
                Map<String, String> params = parsePostParameters(httpExchange);
                String qrCode = params.get("qrCode");
                if (qrCode != null) qrCode = qrCode.trim();
                int position = Integer.parseInt(params.get("position"));

                // find user
                User user = databaseAccess.getUserDatabase().getUser(qrCode);
                if (user == null) {
                    throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);
                }

                for (Session session : clientRegistry.getTableSessions() ) {
                    SetUserOnMapRequest request = messageParser.createMessage(SetUserOnMapRequest.class);
                    request.setUserId(user.getId());
                    request.setUserName(user.getName());
                    switch (position) {
                        case 0:
                            request.setPositionInTeam(0);
                            request.setTeam(Team.RED);
                            break;
                        case 1:
                            request.setPositionInTeam(1);
                            request.setTeam(Team.RED);
                            break;
                        case 2:
                            request.setPositionInTeam(0);
                            request.setTeam(Team.BLUE);
                            break;
                        case 3:
                            request.setPositionInTeam(1);
                            request.setTeam(Team.BLUE);
                            break;
                        default:
                            throw new HTTPException(HttpURLConnection.HTTP_BAD_REQUEST);

                    }
                    session.sendMessage(request);
                }

                writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "OK");
            }
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }


    private void processControlPosition(HttpExchange httpExchange, String path) throws IOException {
        try {
            if (METHOD_POST.equals(httpExchange.getRequestMethod()) && path.trim().length() == 0) {
                Map<String, String> params = parsePostParameters(httpExchange);
                String qrCode = params.get("qrCode");
                if (qrCode != null) qrCode = qrCode.trim();
                int x = parseCoordinate(params.get("x"));
                int y = parseCoordinate(params.get("y"));

                ActionToken actionToken = configuration.getActionToken(qrCode);
                if (actionToken == null) {
                    throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);
                }
                for (Session session : clientRegistry.getTableSessions() ) {
                    TokenLocationUpdateMessage message = messageParser.createMessage(TokenLocationUpdateMessage.class);
                    message.setTokenType(ActionTokenTypeConverter.convert(actionToken.getTokenType()));
                    message.setTokenIndex(actionToken.getIndex());
                    message.setTokenId(actionToken.getQrCode());
                    message.setTeam(TeamConverter.convert(actionToken.getTeam()));
                    message.setLocationX(x);
                    message.setLocationY(y);
                    session.sendMessage(message);
                }

                writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "OK");
            }
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private int parseCoordinate(String nr) {
        int pos = nr.indexOf(".");
        if (pos >= 0) {
            nr = nr.substring(0, pos);
        }
        return Integer.parseInt(nr);
    }

    private void processHtml(HttpExchange httpExchange, String path) throws IOException {
        File file = new File(HTML_DIR + File.separator + path);
        if (file.exists()) {
            byte[] b;
            try (FileInputStream in = new FileInputStream(file)) {
                b = new byte[in.available()];
                in.read(b);
            }
            httpExchange.getResponseHeaders().set("Content-Type", Files.probeContentType(file.toPath()) + "; charset=utf-8");
            httpExchange.getResponseHeaders().set("Cache-Control", "max-age=86400");
            httpExchange.sendResponseHeaders( HttpURLConnection.HTTP_OK, b.length);
            httpExchange.getResponseBody().write(b);
        } else {
            writeResponse(httpExchange, HttpURLConnection.HTTP_NOT_FOUND);
        }

    }

    private void checkRequestMethod(HttpExchange httpExchange, String expectedMethod) {
        if ( !expectedMethod.equals( httpExchange.getRequestMethod() ) ) {
            throw new RuntimeException( "Invalid request method. Expected: " + expectedMethod + ", actual: " + httpExchange.getRequestMethod() );
        }
    }

    private void getStatusRequest( HttpExchange httpExchange ) {
        try {
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "OK");
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void getUserRequest( HttpExchange httpExchange, String idString ) {
        try {
            // get user
            int id = Integer.valueOf(idString);
            User user = databaseAccess.getUserDatabase().getUser(id);
            if (user == null || user.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new UserWrapper(user));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void createUserRequest( HttpExchange httpExchange ) {
        try {
            Map<String, String> params = parsePostParameters(httpExchange);
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                updateUserRequest(httpExchange, id, params);
                return;
            }

            String name = params.get("name");
            String email = params.get("email");
            if (name != null) name = name.trim().toUpperCase();
            if (email != null) email = email.trim();

            // validate
            if (StringUtil.isNull(name)) throw new NullPointerException("Name cannot be null.");
            for (User u : databaseAccess.getUserDatabase().getUsers()) {
                if (u.isHidden()) continue;
                if (name.equals(u.getName()) && (email == null && u.getEmail() == null || email != null && email.equals(u.getEmail()))) {
                    throw new HTTPException(HttpURLConnection.HTTP_CONFLICT);
                }
            }

            User user = databaseAccess.getUserDatabase().addUser(name, email, User.UserType.REGULAR, QrGenerator.generateQr());
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new UserWrapper(user));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Error processing request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void updateUserRequest( HttpExchange httpExchange, String idString ) {
        try {
            Map<String, String> params = parsePostParameters(httpExchange);
            updateUserRequest(httpExchange, idString, params);
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void updateUserRequest( HttpExchange httpExchange, String idString, Map<String, String> params ) {
        try {
            // get user
            int id = Integer.valueOf(idString);
            User user = databaseAccess.getUserDatabase().getUser(id);

            // validate
            if (user == null || user.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);

            // create updated user
            String name = params.get("name");
            String email = params.get("email");
            String internal = params.get("internal");
            user = new User(
                    id,
                    name == null ? user.getName() : name,
                    email == null ? user.getEmail() : email,
                    user.isHidden(),
                    internal == null ? user.getUserType() : User.UserType.getUserType(Integer.valueOf(internal)),
                    user.getQrCode()
            );

            // update
            databaseAccess.getUserDatabase().updateUser(user);

            // send response
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new UserWrapper(user));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteUserRequest( HttpExchange httpExchange, String idString ) {
        try {
            int id = Integer.valueOf(idString);
            User user = databaseAccess.getUserDatabase().getUser(id);

            // validate
            if (user == null || user.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);

            // update hidden
            user = new User(user.getId(), user.getName(), user.getEmail(), true, user.getUserType(), QrGenerator.generateQr());

            // update
            databaseAccess.getUserDatabase().updateUser(user);

            // response
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "OK");
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void listUsersRequest( HttpExchange httpExchange ) {
        try {
            Collection<User> users = databaseAccess.getUserDatabase().getUsers();
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, convertUsers(users));
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void getOccasionRequest( HttpExchange httpExchange, String idString ) {
        try {
            // get user
            int id = Integer.valueOf(idString);
            Occasion occasion = databaseAccess.getOccasionDatabase().getOccasion(id);
            if (occasion == null || occasion.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new OccasionWrapper(occasion));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void createOccasionRequest( HttpExchange httpExchange ) {
        try {
            Map<String, String> params = parsePostParameters(httpExchange);
            String description = params.get("description");
            if (description != null) description = description.trim();

            // validate
            if (StringUtil.isNull(description)) throw new NullPointerException("Description cannot be null.");
            for (Occasion o : databaseAccess.getOccasionDatabase().listOccasions()) {
                if (o.isHidden()) continue;
                if (description.equalsIgnoreCase(o.getDescription())) {
                    throw new HTTPException(HttpURLConnection.HTTP_CONFLICT);
                }
            }

            Occasion occasion = databaseAccess.getOccasionDatabase().createOccasion(description);
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new OccasionWrapper(occasion));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Error processing request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void updateOccasionRequest( HttpExchange httpExchange, String idString ) {
        try {
            Map<String, String> params = parsePostParameters(httpExchange);

            // get
            int id = Integer.valueOf(idString);
            Occasion occasion = databaseAccess.getOccasionDatabase().getOccasion(id);

            // validate
            if (occasion == null || occasion.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);

            // create updated object
            String description = params.get("description");
            occasion = new Occasion(
                    id,
                    description == null ? occasion.getDescription() : description,
                    occasion.isHidden()
            );

            // update
            databaseAccess.getOccasionDatabase().updateOccasion(occasion);

            // send response
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new OccasionWrapper(occasion));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteOccasionRequest( HttpExchange httpExchange, String idString ) {
        try {
            int id = Integer.valueOf(idString);
            Occasion occasion = databaseAccess.getOccasionDatabase().getOccasion(id);

            // validate
            if (occasion == null || occasion.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);

            // update hidden
            occasion = new Occasion(occasion.getOccasionId(), occasion.getDescription(), true);

            // update
            databaseAccess.getOccasionDatabase().updateOccasion(occasion);

            // response
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "OK");
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void listOccasionsRequest( HttpExchange httpExchange ) {
        try {
            Collection<Occasion> occasions = databaseAccess.getOccasionDatabase().listOccasions();
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, convertOccasions(occasions));
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void updateCurrentOccasionRequest( HttpExchange httpExchange, String idString ) {
        try {
            // get occasion
            int id = Integer.valueOf(idString);
            Occasion occasion = databaseAccess.getOccasionDatabase().getOccasion(id);

            // validate
            if (occasion == null || occasion.isHidden()) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);

            // update
            databaseAccess.getOccasionDatabase().setCurrentOccasion(occasion);

            // send response
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new OccasionWrapper(occasion));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void getCurrentOccasionRequest( HttpExchange httpExchange, String idString ) {
        try {
            // get user
            Occasion occasion = databaseAccess.getOccasionDatabase().getCurrentOccasion();
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, new OccasionWrapper(occasion));
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void listLeaderboard( HttpExchange httpExchange ) {
        try {
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, ListLeaderBoardTask.execute(databaseAccess));
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void getUserRating( HttpExchange httpExchange, String idString ) {
        try {
            // get user
            int id = Integer.valueOf(idString);
            EloRating rating = databaseAccess.getRatingDatabase().getRating(id);
            if (rating.getMatches() == 0) throw new HTTPException(HttpURLConnection.HTTP_NOT_FOUND);
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, rating);
        } catch (HTTPException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, e.getStatusCode());
        } catch (Exception e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void processMockUsers( HttpExchange httpExchange, String path ) {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        int position = 0;
        if (path != null && path.trim().length() > 0) {
            try {
                position = Integer.parseInt(path.trim());
            } catch (NumberFormatException e) {}
        }
        if (position < 1) {
            users.add(new RegisteredUser(1, "test1", "test1@gmail.com"));
        } else if (position < 2) {
            users.add(new RegisteredUser(1, "test1", "test1@gmail.com"));
            users.add(new RegisteredUser(2, "test2", "test2@gmail.com"));
        } else if (position < 3) {
            users.add(new RegisteredUser(3, "test3", "test3@gmail.com"));
        }
        writeResponse(httpExchange, HttpURLConnection.HTTP_OK, users);
    }

    private void processLeaderboardPush(HttpExchange httpExchange, String path ) {
        try {
            byte[] data = readPostData(httpExchange);
            logger.info("Pushed leaderboard: " + new String(data, ENCODING));
            writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "OK");
        } catch (IOException e) {
            logger.log(Level.INFO, "Invalid request", e);
            writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void writeResponse( HttpExchange httpExchange, int errorCode, Object response ) {
        try {
            ResponseWrapper rw = new ResponseWrapper(response);
            byte[] b = rw.toBytes();
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            httpExchange.sendResponseHeaders( errorCode, b.length);
            httpExchange.getResponseBody().write( b);
        } catch (Exception e) {
            logger.log(Level.INFO, "Unable to send response", e);
        }
    }

    private void writeResponse( HttpExchange httpExchange, int errorCode ) {
        try {
            ResponseWrapper rw = new ResponseWrapper("Bad request");
            byte[] b = rw.toBytes();
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            httpExchange.sendResponseHeaders( errorCode, b.length);
            httpExchange.getResponseBody().write( b);
        } catch (Exception e) {
            logger.log(Level.INFO, "Unable to send response", e);
        }
    }

    public void stop() {
        if (s.getExecutor()==null)//stop before start
            return;

        Executor ex = s.getExecutor();
        if (ex instanceof ExecutorService){
            ((ExecutorService) ex).shutdown();
        }
        s.stop(0);
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Stopped HttpUtilityServer");
        }
    }

    private static Executor createExecutor() {
        ThreadPoolExecutor e = new ThreadPoolExecutor(1, 2, 6, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1024));
        ThreadGroup g = Thread.currentThread().getThreadGroup();
        e.setThreadFactory(new ThreadFactory() {
            AtomicInteger count = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(g, r, String.format("http-%d", count.incrementAndGet()));
            }
        });
        return e;
    }

    private static URI relative(HttpExchange httpExchange) throws IOException{
        try{
            return new URI(httpExchange.getHttpContext().getPath()).relativize(httpExchange.getRequestURI());
        } catch(URISyntaxException _x){
            throw new IOException(_x);
        }
    }

//	private static Map<String, String> parseQueryParameters(HttpExchange httpExchange) {
//		return parseJsonParameters(httpExchange.getRequestURI().getQuery());
//	}

    private static Map<String, String> parsePostParameters(HttpExchange httpExchange) throws IOException {
        // lets get post parameters
        byte[] messageBody = readPostData(httpExchange);

        // lets parse parameters string
        return parseJsonParameters(new String(messageBody, ENCODING));
    }

    private static byte[] readPostData(HttpExchange httpExchange) throws IOException {
        ByteArrayOutputStream messageBody = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        InputStream in = httpExchange.getRequestBody();
        int pos;
        while ( (pos = in.read(buffer)) != -1 ) {
            messageBody.write(buffer, 0, pos);
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("HttpUtilityServer post parameters: %s", new String(messageBody.toByteArray(), ENCODING)));
        }
        return messageBody.toByteArray();
    }

    private static Map<String, String> parseJsonParameters(String paramStr) {
        Map<String, String> queryParameters = new LinkedHashMap<>();
        String[] parameters = Optional.of(paramStr).orElse("").split("&"); // key1=value1&key2=value2&...x=&y=111&&&
        for (String p: parameters) {
            if (p.length()==0)
                continue;

            final int idx = p.indexOf('=');

            if (idx==0){//skip =value (i.e. no key)
                continue;
            }
            String key = idx<0? p : p.substring(0, idx);
            String value = idx<0? "" : p.substring(idx+1, p.length());

            try {
                queryParameters.put(key, URLDecoder.decode(value, ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new SystemException("Unable to decode: " + value, e);
            }
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("HttpUtilityServer uri parameters: %s", queryParameters));
        }
        return queryParameters;
    }

    private Collection<UserWrapper> convertUsers(Collection<User> users) {
        ArrayList<UserWrapper> wrapped = new ArrayList<>();
        for (User user : users) {
            if (!user.isHidden()) {
                wrapped.add(new UserWrapper(user));
            }
        }
        return wrapped;
    }

    private Collection<OccasionWrapper> convertOccasions(Collection<Occasion> occasions) {
        ArrayList<OccasionWrapper> wrapped = new ArrayList<>();
        for (Occasion occasion : occasions) {
            if (!occasion.isHidden()) {
                wrapped.add(new OccasionWrapper(occasion));
            }
        }
        return wrapped;
    }

}
