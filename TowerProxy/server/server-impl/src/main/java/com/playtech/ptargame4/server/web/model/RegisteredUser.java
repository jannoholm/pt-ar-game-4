package com.playtech.ptargame4.server.web.model;

public class RegisteredUser {
    private int position;
    private String name;
    private String email;
    private String qrCode;
    private String information;

    public RegisteredUser() {
    }

    public RegisteredUser(int position, String name, String email, String information) {
        this.position = position;
        this.name = name;
        this.email = email;
        this.information = information;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	@Override
    public String toString() {
        return "RegisteredUser{" +
                "position=" + position +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", information='" + information + '\'' +
                '}';
    }
}
