package com.playtech.ptargame4.api.table;

import java.nio.ByteBuffer;

import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.api.AbstractRequest;
import com.playtech.ptargame4.api.lobby.Team;

public class SetUserOnMapRequest extends AbstractRequest {

	private int userId;
	private String userName;
	private Team team;
	private int positionInTeam;

	public SetUserOnMapRequest(MessageHeader header) {
		super(header);
	}

	public void parse(ByteBuffer messageData) {
		super.parse(messageData);
		userId = messageData.getInt();
		userName = StringUtil.readUTF8String(messageData);
		team = Team.values()[messageData.get()];
		positionInTeam = messageData.getInt();
	}

	public void format(ByteBuffer messageData) {
		super.format(messageData);
		messageData.putInt(userId);
		StringUtil.writeUTF8String(userName, messageData);
		messageData.put((byte) team.ordinal());
		messageData.putInt(positionInTeam);
	}

	protected void toStringImpl(StringBuilder s) {
		super.toStringImpl(s);
		s.append(", userId=").append(userId);
		s.append(", userName=").append(userName);
		s.append(", team=").append(team);
		s.append(", positionInTeam=").append(positionInTeam);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getPositionInTeam() {
		return positionInTeam;
	}

	public void setPositionInTeam(int positionInTeam) {
		this.positionInTeam = positionInTeam;
	}
}
