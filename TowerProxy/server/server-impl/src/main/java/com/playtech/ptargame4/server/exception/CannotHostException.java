package com.playtech.ptargame4.server.exception;

import com.playtech.ptargame4.api.ApiConstants;
import com.playtech.ptargame.common.exception.ApiException;


public class CannotHostException extends ApiException {
    public CannotHostException(String message) {
        super(ApiConstants.ERR_GAME_HOST, message);
    }

    public CannotHostException(String message, Throwable cause) {
        super(ApiConstants.ERR_GAME_HOST, message, cause);
    }
}
