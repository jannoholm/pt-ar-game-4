package com.playtech.ptargame4.server.exception;


import com.playtech.ptargame.common.exception.ApiException;
import com.playtech.ptargame4.api.ApiConstants;

public class SystemException extends ApiException {
    public SystemException(String message) {
        super(ApiConstants.ERR_SYSTEM, message);
    }

    public SystemException(String message, Throwable cause) {
        super(ApiConstants.ERR_SYSTEM, message, cause);
    }
}
