package com.playtech.ptargame4.api;

import com.playtech.ptargame4.api.leaderboard.GetLeaderboardRequest;
import com.playtech.ptargame4.api.leaderboard.GetLeaderboardResponse;
import com.playtech.ptargame4.api.table.CarControlMessage;
import com.playtech.ptargame4.api.table.GameResultStoreRequest;
import com.playtech.ptargame4.api.table.GameResultStoreResponse;
import com.playtech.ptargame4.api.table.GetUsersRequest;
import com.playtech.ptargame4.api.table.GetUsersResponse;
import com.playtech.ptargame4.api.table.LocationNotificationMessage;
import com.playtech.ptargame4.api.game.GameUpdateBroadcastMessage;
import com.playtech.ptargame4.api.lobby.PushGameLobbyUpdateMessage;
import com.playtech.ptargame4.api.table.SetUserInCarRequest;
import com.playtech.ptargame4.api.table.SetUserInCarResponse;
import com.playtech.ptargame4.api.table.SetUserOnMapRequest;
import com.playtech.ptargame4.api.table.SetUserOnMapResponse;
import com.playtech.ptargame4.api.token.TokenLocationUpdateMessage;
import com.playtech.ptargame.common.message.Message;
import com.playtech.ptargame.common.message.MessageFactory;
import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame4.api.game.GameControlMessage;
import com.playtech.ptargame4.api.game.GameUpdateMessage;
import com.playtech.ptargame4.api.general.PingRequest;
import com.playtech.ptargame4.api.general.PingResponse;
import com.playtech.ptargame4.api.lobby.GetDetailedGameInfoRequest;
import com.playtech.ptargame4.api.lobby.GetDetailedGameInfoResponse;
import com.playtech.ptargame4.api.lobby.GetGamesRequest;
import com.playtech.ptargame4.api.lobby.GetGamesResponse;
import com.playtech.ptargame4.api.lobby.HostGameRequest;
import com.playtech.ptargame4.api.lobby.HostGameResponse;
import com.playtech.ptargame4.api.lobby.HostTableGameRequest;
import com.playtech.ptargame4.api.lobby.HostTableGameResponse;
import com.playtech.ptargame4.api.lobby.JoinGameRequest;
import com.playtech.ptargame4.api.lobby.JoinGameResponse;
import com.playtech.ptargame4.api.general.JoinServerRequest;
import com.playtech.ptargame4.api.general.JoinServerResponse;

import java.util.HashMap;
import java.util.Map;

public class ProxyMessageFactory implements MessageFactory {

    private final Map<Integer, Class<? extends Message>> typeToClass = new HashMap<>();
    private final Map<Class<? extends Message>, Integer> classToType = new HashMap<>();

    public void initialize() {
        // general
        addMessage(1000, PingRequest.class);
        addMessage(1001, PingResponse.class);
        addMessage(1002, JoinServerRequest.class);
        addMessage(1003, JoinServerResponse.class);

        // lobby
        addMessage(2000, GetGamesRequest.class);
        addMessage(2001, GetGamesResponse.class);
        addMessage(2004, GetDetailedGameInfoRequest.class);
        addMessage(2005, GetDetailedGameInfoResponse.class);
        addMessage(2006, JoinGameRequest.class);
        addMessage(2007, JoinGameResponse.class);
        addMessage(2008, HostGameRequest.class);
        addMessage(2009, HostGameResponse.class);
        addMessage(2010, PushGameLobbyUpdateMessage.class);
        addMessage(2011, HostTableGameRequest.class);
        addMessage(2012, HostTableGameResponse.class);

        // game-play
        addMessage(3000, GameControlMessage.class);
        addMessage(3002, GameUpdateBroadcastMessage.class);
        addMessage(3004, GameUpdateMessage.class);
        addMessage(3006, LocationNotificationMessage.class);
        addMessage(3008, CarControlMessage.class);
        addMessage(3010, GetUsersRequest.class);
        addMessage(3011, GetUsersResponse.class);
        addMessage(3012, SetUserInCarRequest.class);
        addMessage(3013, SetUserInCarResponse.class);
        addMessage(3014, GameResultStoreRequest.class);
        addMessage(3015, GameResultStoreResponse.class);
        addMessage(3100, SetUserOnMapRequest.class);
        addMessage(3101, SetUserOnMapResponse.class);
        
        // leaderboard
        addMessage(4000, GetLeaderboardRequest.class);
        addMessage(4001, GetLeaderboardResponse.class);
        
        // AR tokens
        addMessage(5000, TokenLocationUpdateMessage.class);
    }

    private void addMessage(int messageType, Class<? extends Message> messageClass) {
        typeToClass.put(messageType, messageClass);
        classToType.put(messageClass, messageType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Message createMessage(MessageHeader header) {
        Class<? extends Message> messageClass = typeToClass.get(header.getMessageType());
        if (messageClass == null) {
            throw new IllegalArgumentException("Unknown message type: " + header.getMessageType());
        }
        try {
            return messageClass.getConstructor(MessageHeader.class).newInstance(header);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid message class: " + messageClass.getName(), e);
        }
    }

    @Override
    public <T extends Message> int getMessageType(Class<T> messageClass) {
        Integer messageType = classToType.get(messageClass);
        if (messageType == null) {
            throw new IllegalArgumentException("Unknown message class: " + messageClass);
        }
        return messageType;
    }

}
