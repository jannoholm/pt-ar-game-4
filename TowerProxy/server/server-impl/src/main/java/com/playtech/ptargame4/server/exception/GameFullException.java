package com.playtech.ptargame4.server.exception;

import com.playtech.ptargame4.api.ApiConstants;
import com.playtech.ptargame.common.exception.ApiException;


public class GameFullException extends ApiException {
    public GameFullException(String message) {
        super(ApiConstants.ERR_GAME_FULL, message);
    }

    public GameFullException(String message, Throwable cause) {
        super(ApiConstants.ERR_GAME_FULL, message, cause);
    }
}
