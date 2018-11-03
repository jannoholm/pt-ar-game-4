package com.playtech.ptargame4.server.conf.model;

public final class EndPoints {
    private final String pollUsersUrl;
    private final String pushLeaderboardUrl;

    public EndPoints(String pollUsersUrl, String pushLeaderboardUrl) {
        this.pollUsersUrl = pollUsersUrl;
        this.pushLeaderboardUrl = pushLeaderboardUrl;
    }

    public String getPollUsersUrl() {
        return pollUsersUrl;
    }

    public String getPushLeaderboardUrl() {
        return pushLeaderboardUrl;
    }

    @Override
    public String toString() {
        return "EndPoints{" +
                "pollUsersUrl='" + pollUsersUrl + '\'' +
                ", pushLeaderboardUrl='" + pushLeaderboardUrl + '\'' +
                '}';
    }
}
