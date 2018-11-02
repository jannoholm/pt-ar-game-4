package com.playtech.ptargame4.server.conf.model;

import com.playtech.ptargame4.server.exception.SystemException;
import com.playtech.ptargame4.server.registry.GameRegistryGame;

public class ActionToken {

    public enum TokenType {
        BRIDGE,
        SPAWN;

        public static TokenType getTokenType(String typeStr) {
            for (TokenType type : values()) {
                if (type.toString().equals(typeStr)) {
                    return type;
                }
            }
            throw new SystemException("ActionToken type '" + typeStr + "' not found!");
        }
    }

    private final String qrCode;
    private final TokenType tokenType;
    private final byte index;
    private final GameRegistryGame.Team team;

    public ActionToken(String qrCode, TokenType tokenType, byte index, GameRegistryGame.Team team) {
        this.qrCode = qrCode;
        this.tokenType = tokenType;
        this.index = index;
        this.team = team;
    }

    public String getQrCode() {
        return qrCode;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public byte getIndex() {
        return index;
    }

    public GameRegistryGame.Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "ActionToken{" +
                "qrCode='" + qrCode + '\'' +
                ", tokenType=" + tokenType +
                ", index=" + index +
                ", team=" + team +
                '}';
    }
}
