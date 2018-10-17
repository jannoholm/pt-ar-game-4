package com.playtech.ptargame4.server.exception;

import com.playtech.ptargame4.api.ApiConstants;
import com.playtech.ptargame.common.exception.ApiException;

public class BotNotConnectedException extends ApiException {
    public BotNotConnectedException(String message) {
        super(ApiConstants.ERR_BOT_NOT_CONNECTED, message);
    }

    public BotNotConnectedException(String message, Throwable cause) {
        super(ApiConstants.ERR_BOT_NOT_CONNECTED, message, cause);
    }
}
