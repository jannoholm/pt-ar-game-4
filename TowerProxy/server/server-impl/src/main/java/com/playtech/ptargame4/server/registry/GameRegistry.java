package com.playtech.ptargame4.server.registry;


import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.server.exception.GameNotFoundException;
import com.playtech.ptargame4.server.session.ClientListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GameRegistry implements ClientListener {
	
    private static final Logger logger = Logger.getLogger(GameRegistry.class.getName());

    private final ScheduledExecutorService executor;
    private ScheduledFuture maintenanceFuture;

    private final ConcurrentHashMap<String, GameRegistryGame> games = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GameRegistryGame> hosting = new ConcurrentHashMap<>();

    public GameRegistry(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void start() {
        if (maintenanceFuture == null) {
            maintenanceFuture = executor.scheduleAtFixedRate(() -> {
                long time = System.currentTimeMillis();
                for (GameRegistryGame game : games.values()) {
                    if (!game.isHostConnected() && game.getHostDisconnectTime()+5*60*1000 < time) {
                        removeGame(game.getGameId());
                    }
                }
            }, 500, 515, TimeUnit.MILLISECONDS);
        } else {
            throw new IllegalStateException("Already started callback handler.");
        }
    }

    public void stop() {
        maintenanceFuture.cancel(true);
    }

    public GameRegistryGame getGame(String gameId) {
        GameRegistryGame game = games.get(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game not found: " + gameId);
        }
        return game;
    }

    public synchronized String createGame(String clientId, String gameName, int players, boolean joinAsPlayer, String aiType, boolean tableGame) {
        GameRegistryGame game = hosting.get(clientId);
        if (game != null) {
            logger.info("Existing game for clientId=" + clientId + ", gameId=" + game.getGameId());
            return game.getGameId();
        }

        // create new game
        GameRegistryGame newGame = new GameRegistryGame(clientId, UUID.randomUUID().toString(), gameName, players, aiType, tableGame);
        if (joinAsPlayer) {
            newGame.addPlayer(clientId, GameRegistryGame.Team.RED, -1);
        }

        // store
        hosting.put(clientId, newGame);
        games.put(newGame.getGameId(), newGame);
        
        logger.info("Created new game with clientId=" + clientId + ", gameId=" + newGame.getGameId());
        
        return newGame.getGameId();
    }

    public Collection<GameRegistryGame> findGames(String filter, boolean all) {
        ArrayList<GameRegistryGame> matching = new ArrayList<>();
        for (GameRegistryGame game : games.values()) {
            if (!(game.getGameStatus() == GameRegistryGame.Status.COLLECTING || game.getGameStatus() == GameRegistryGame.Status.COLLECTING && all)) continue;
            if (!(game.isHostConnected() || all)) continue;
            if (!(StringUtil.isNull(filter) || game.getGameName().contains(filter))) continue;
            if (!(game.getPlayers().size() < game.getPositions() || all)) continue;
            matching.add(game);
            if (matching.size() > 9) break;
        }
        return matching;
    }

    @Override
    public void clientConnected(String clientId, int userId) {
        if (clientId == null) return;
        GameRegistryGame game = hosting.get(clientId);
        if (game != null) {
            game.setHostConnected(true);
        }
    }

    @Override
    public void clientDisconnected(String clientId, int userId) {
        if (clientId == null) return;
        GameRegistryGame game = hosting.get(clientId);
        if (game != null) {
            game.setHostConnected(false);
        }
    }

    private void removeGame(String gameId) {
        GameRegistryGame game = games.remove(gameId);
        if (game != null) {
            hosting.remove(game.getHostClientId());
        }
    }
}
