package com.playtech.ptargame4.server.database;


import com.playtech.ptargame4.server.database.model.User;

import java.util.Collection;

public interface UserDatabase {

    User addUser(String name, String email, User.UserType userType, String qrCode, String information);

    User getUser(int id);

    User getUser(String qrCode);

    void updateUser(User user);

    Collection<User> getUsers();

    Collection<User> getUsers(String filter);

    Collection<User> getUsersByName(String name);

}
