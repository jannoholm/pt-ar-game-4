package com.playtech.ptargame4.server.conf.model;

public final class EndPoints {
    private final String pollUsersUrl;
    private final String pushDashUrl;

    public EndPoints(String pollUsersUrl, String pushDashUrl) {
        this.pollUsersUrl = pollUsersUrl;
        this.pushDashUrl = pushDashUrl;
    }

    public String getPollUsersUrl() {
        return pollUsersUrl;
    }

    public String getPushDashUrl() {
        return pushDashUrl;
    }

    @Override
    public String toString() {
        return "EndPoints{" +
                "pollUsersUrl='" + pollUsersUrl + '\'' +
                ", pushDashUrl='" + pushDashUrl + '\'' +
                '}';
    }
}
