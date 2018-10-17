package com.playtech.ptargame4.server.exception;

import com.playtech.ptargame4.api.ApiConstants;
import com.playtech.ptargame.common.exception.ApiException;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(ApiConstants.ERR_USER_NOT_FOUND, message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(ApiConstants.ERR_USER_NOT_FOUND, message, cause);
    }
}
