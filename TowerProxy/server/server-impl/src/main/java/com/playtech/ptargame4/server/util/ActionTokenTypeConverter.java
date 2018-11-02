package com.playtech.ptargame4.server.util;

import com.playtech.ptargame4.api.token.TokenType;
import com.playtech.ptargame4.server.conf.model.ActionToken;

public class ActionTokenTypeConverter {
    public static ActionToken.TokenType convert(TokenType apiType) {
        switch (apiType) {
            case SPAWN:
                return ActionToken.TokenType.SPAWN;
            case BRIDGE:
                return ActionToken.TokenType.BRIDGE;
            default:
                throw new IllegalArgumentException("Unknown token type: " + apiType);
        }
    }
    public static TokenType convert(ActionToken.TokenType internalType) {
        switch (internalType) {
            case SPAWN:
                return TokenType.SPAWN;
            case BRIDGE:
                return TokenType.BRIDGE;
            default:
                throw new IllegalArgumentException("Unknown token type: " + internalType);
        }
    }
}
