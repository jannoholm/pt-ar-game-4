package com.playtech.ptargame4.server.database.model;


public class User {

    public enum UserType {
        REGULAR,
        INTERNAL,
        BOT;

        public static UserType getUserType(int ut) {
            return values()[ut];
        }
    }

    private final int id;
    private final String name;
    private final String email;
    private final boolean hidden;
    private final UserType userType;
    private final String qrCode;
	private final String information;

    public User(int id, String name, String email, boolean hidden, UserType userType, String qrCode, String information) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hidden = hidden;
        this.userType = userType;
        this.qrCode = qrCode;
		this.information = information;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isHidden() {
        return hidden;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getQrCode() {
        return qrCode;
    }
	
	public String getInformation() {
        return information;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hidden='" + hidden + '\'' +
                ", userType='" + userType + '\'' +
                ", qrCode='" + qrCode + '\'' +
				", information='" + information + '\'' +
                '}';
    }
}
