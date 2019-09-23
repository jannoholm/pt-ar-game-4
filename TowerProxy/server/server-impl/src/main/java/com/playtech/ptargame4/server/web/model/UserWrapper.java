package com.playtech.ptargame4.server.web.model;

import com.playtech.ptargame4.server.database.model.User;

public class UserWrapper {
	private final int id;
	private final String name;
	private final String email;
	private final String qrCode;
	private final String information;

	public UserWrapper(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.qrCode = user.getQrCode();
		this.information = user.getInformation();
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

	public String getQrCode() {
		return qrCode;
	}

	public String getInformation() {
		return information;
	}

}
