package com.playtech.ptargame4.server.web.model;

public class RegisteredUser {
    private int position;
    private String name;
    private String email;

    public RegisteredUser() {
    }

    public RegisteredUser(int position, String name, String email) {
        this.position = position;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "position=" + position +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
