package com.playtech.ptargame4.api.table;


import com.playtech.ptargame4.api.AbstractRequest;
import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.StringUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

public class GameResultStoreRequest extends AbstractRequest {

    public enum WinnerTeam {
        DRAW,
        RED,
        BLUE
    }

    private String gameId;
    private WinnerTeam winnerTeam;
    private int gameTime;
    private Collection<GameResultPlayerActivity> playerResults = new ArrayList<>();

    public GameResultStoreRequest(MessageHeader header) {
        super(header);
    }
    @Override
    public void parse(ByteBuffer messageData) {
        super.parse(messageData);
        gameId = StringUtil.readUTF8String(messageData);
        winnerTeam = WinnerTeam.values()[messageData.get()];
        gameTime = messageData.getInt();
        int size = messageData.getInt();
        for (int i = 0; i < size; ++i) {
            GameResultPlayerActivity playerInfo = new GameResultPlayerActivity(getHeader());
            playerInfo.parse(messageData);
            playerResults.add(playerInfo);
        }
    }

    @Override
    public void format(ByteBuffer messageData) {
        super.format(messageData);
        StringUtil.writeUTF8String(gameId, messageData);
        messageData.put((byte)winnerTeam.ordinal());
        messageData.putInt(gameTime);
        messageData.putInt(playerResults.size());
        for (GameResultPlayerActivity playerInfo : playerResults) {
            playerInfo.format(messageData);
        }
    }

    @Override
    protected void toStringImpl(StringBuilder s) {
        super.toStringImpl(s);
        s.append(", gameId=").append(gameId);
        s.append(", winnerTeam=").append(winnerTeam);
        s.append(", gameTime=").append(gameTime);
        s.append(", players={");
        for (GameResultPlayerActivity playerInfo : playerResults) {
            s.append("(");
            playerInfo.toStringImpl(s);
            s.append(")");
        }
        s.append("}");
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public WinnerTeam getWinnerTeam() {
        return winnerTeam;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public void setWinnerTeam(WinnerTeam winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public Collection<GameResultPlayerActivity> getPlayerResults() {
        return playerResults;
    }

    public void addPlayerResult(GameResultPlayerActivity playerResult) {
        this.playerResults.add( playerResult );
    }
}
