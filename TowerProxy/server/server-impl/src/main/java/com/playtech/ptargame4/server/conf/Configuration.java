package com.playtech.ptargame4.server.conf;

import com.playtech.ptargame4.server.conf.model.ActionToken;
import com.playtech.ptargame4.server.conf.model.EndPoints;

public interface Configuration {

    int getWebPort();

    int getBinaryPort();

    EndPoints getEndpoints();

    ActionToken getActionToken(String qrCode);

}
