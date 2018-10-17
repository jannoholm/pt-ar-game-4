package com.playtech.ptargame4.server.exception;

import com.playtech.ptargame4.api.ApiConstants;
import com.playtech.ptargame.common.exception.ApiException;


public class GameNotFoundException extends ApiException {
    public GameNotFoundException(String message) {
        super(ApiConstants.ERR_GAME_NOT_FOUND, message);
    }

    public GameNotFoundException(String message, Throwable cause) {
        super(ApiConstants.ERR_GAME_NOT_FOUND, message, cause);
    }
}
