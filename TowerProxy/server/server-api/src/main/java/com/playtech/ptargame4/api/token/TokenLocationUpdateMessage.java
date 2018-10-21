package com.playtech.ptargame4.api.token;

import java.nio.ByteBuffer;

import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame.common.util.StringUtil;
import com.playtech.ptargame4.api.AbstractMessage;
import com.playtech.ptargame4.api.lobby.Team;

public class TokenLocationUpdateMessage extends AbstractMessage {

	public TokenLocationUpdateMessage(MessageHeader header) {
		super(header);
	}

	private String tokenId;
	private Team team;
	private TokenType tokenType;
	private byte tokenIndex;
	private int locationX;
	private int locationY;

	public void parse(ByteBuffer messageData) {
		super.parse(messageData);
		tokenId = StringUtil.readUTF8String(messageData);
		team = Team.values()[messageData.get()];
		tokenType = TokenType.values()[messageData.get()];
		tokenIndex = messageData.get();
		locationX = messageData.getInt();
		locationY = messageData.getInt();
	}

	public void format(ByteBuffer messageData) {
		super.format(messageData);
		StringUtil.writeUTF8String(tokenId, messageData);
		messageData.put((byte) team.ordinal());
		messageData.put((byte) tokenType.ordinal());
		messageData.put(tokenIndex);
		messageData.putInt(locationX);
		messageData.putInt(locationY);
	}

	protected void toStringImpl(StringBuilder s) {
		super.toStringImpl(s);
		s.append(", tokenId=").append(tokenId);
		s.append(", team=").append(team);
		s.append(", tokenType=").append(tokenType);
		s.append(", tokenIndx=").append(tokenIndex);
		s.append(", locationX=").append(locationX);
		s.append(", locationY=").append(locationY);
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public byte getTokenIndex() {
		return tokenIndex;
	}

	public void setTokenIndex(byte tokenIndex) {
		this.tokenIndex = tokenIndex;
	}

	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
}
