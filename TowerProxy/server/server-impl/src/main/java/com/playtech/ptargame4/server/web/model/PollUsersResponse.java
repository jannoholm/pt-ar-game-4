package com.playtech.ptargame4.server.web.model;

import java.util.List;

public class PollUsersResponse {
    private List<RegisteredUser> data;

    public List<RegisteredUser> getData() {
        return data;
    }

    public void setData(List<RegisteredUser> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PollUsersResponse{" +
                "data=" + data +
                '}';
    }
}
