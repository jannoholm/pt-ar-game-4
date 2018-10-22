package com.playtech.ptargame4.server.database;


public interface DatabaseAccess {

    UserDatabase getUserDatabase();

    GameDatabase getGameDatabase();

    RatingDatabase getRatingDatabase();

    ActionTokenDatabase getActionTokenDatabase();

}
