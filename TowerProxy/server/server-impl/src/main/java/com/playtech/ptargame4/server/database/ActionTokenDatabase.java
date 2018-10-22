package com.playtech.ptargame4.server.database;

import com.playtech.ptargame4.server.database.model.ActionToken;

public interface ActionTokenDatabase {

    ActionToken getActionToken(String qrCode);

}
