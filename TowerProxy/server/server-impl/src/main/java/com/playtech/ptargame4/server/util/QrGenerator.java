package com.playtech.ptargame4.server.util;

import com.playtech.ptargame4.server.exception.SystemException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class QrGenerator {

    private static SecureRandom random;
    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException("Unable to initialize random!");
        }
    }

    public static String generateQr(){
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

}
