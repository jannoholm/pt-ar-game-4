package com.playtech.ptargame4.server.web.task.dashboard;

import com.playtech.ptargame4.server.database.DatabaseAccess;
import com.playtech.ptargame4.server.database.model.EloRating;
import com.playtech.ptargame4.server.database.model.User;
import com.playtech.ptargame4.server.web.model.LeaderboardWrapper;

import java.util.ArrayList;
import java.util.Collection;

public class ListLeaderBoardTask {

    public static Collection<LeaderboardWrapper> execute(DatabaseAccess databaseAccess) {
        Collection<EloRating> leaderboard = databaseAccess.getRatingDatabase().getLeaderboard();
        Collection<User> users = databaseAccess.getUserDatabase().getUsers();
        return convertLeaderboard(leaderboard, users);
    }

    private static Collection<LeaderboardWrapper> convertLeaderboard(Collection<EloRating> leaderboard, Collection<User> users) {
        ArrayList<LeaderboardWrapper> wrapped = new ArrayList<>();
        int pos = 0;
        for (EloRating rating : leaderboard) {
            pos++;
            for (User user : users) {
                if (user.getId() == rating.getUserId() && user.getUserType() == User.UserType.REGULAR && !user.isHidden()) {
                    LeaderboardWrapper wrapper = new LeaderboardWrapper(user.getName(), rating, pos);
                    wrapped.add(wrapper);
                    break;
                }
            }
        }
        return wrapped;
    }

}
