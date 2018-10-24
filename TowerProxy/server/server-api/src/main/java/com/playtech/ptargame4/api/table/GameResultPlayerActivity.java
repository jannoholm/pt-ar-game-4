package com.playtech.ptargame4.api.table;


import com.playtech.ptargame.common.message.MessageHeader;
import com.playtech.ptargame4.api.AbstractRequest;
import com.playtech.ptargame4.api.lobby.Team;

import java.nio.ByteBuffer;

public class GameResultPlayerActivity extends AbstractRequest {

    private int userId;
    private Team team;
    private byte positionInTeam;
    private int towerHealth;
    private int towerEnemeyHealth;
    private int bridgesBuilt;
    private int bridgesBuiltPoints;
    private int bridgesDestroyed;
    private int bridgesDestroyedPoints;
    private int bridgeSoldierSaves;
    private int bridgeSoldierDeaths;
    private int bridgeSoldierEnemySaves;
    private int bridgeSoldierEnemyKills;
    

    public GameResultPlayerActivity(MessageHeader header) {
        super(header);
    }


    public void parse(ByteBuffer messageData) {
        super.parse(messageData);
        
        // message data
        userId = messageData.getInt();
        team = Team.values()[messageData.get()];
        positionInTeam = messageData.get();
        towerHealth = messageData.getInt();
        towerEnemeyHealth = messageData.getInt();
        bridgesBuilt = messageData.getInt();
        bridgesBuiltPoints = messageData.getInt();
        bridgesDestroyed = messageData.getInt();
        bridgesDestroyedPoints = messageData.getInt();
        bridgeSoldierSaves = messageData.getInt();
        bridgeSoldierDeaths = messageData.getInt();
        bridgeSoldierEnemySaves = messageData.getInt();
        bridgeSoldierEnemyKills = messageData.getInt();
    }

    public void format(ByteBuffer messageData) {
		super.format(messageData);

        // data
        messageData.putInt(userId);
        messageData.put((byte)team.ordinal());
        messageData.put(positionInTeam);
        messageData.putInt(towerHealth);
        messageData.putInt(towerEnemeyHealth);
        messageData.putInt(bridgesBuilt);
        messageData.putInt(bridgesBuiltPoints);
        messageData.putInt(bridgesDestroyed);
        messageData.putInt(bridgesDestroyedPoints);
        messageData.putInt(bridgeSoldierSaves);
        messageData.putInt(bridgeSoldierDeaths);
        messageData.putInt(bridgeSoldierEnemySaves);
        messageData.putInt(bridgeSoldierEnemyKills);
    }

    protected void toStringImpl(StringBuilder builder) {
		builder.append("userId=");
		builder.append(userId);
		builder.append(", team=");
		builder.append(team);
		builder.append(", positionInTeam=");
		builder.append(positionInTeam);
		builder.append(", towerHealth=");
		builder.append(towerHealth);
		builder.append(", towerEnemeyHealth=");
		builder.append(towerEnemeyHealth);
		builder.append(", bridgesBuilt=");
		builder.append(bridgesBuilt);
		builder.append(", bridgesBuiltPoints=");
		builder.append(bridgesBuiltPoints);
		builder.append(", bridgesDestroyed=");
		builder.append(bridgesDestroyed);
		builder.append(", bridgesDestroyedPoints=");
		builder.append(bridgesDestroyedPoints);
		builder.append(", bridgeSoldierSaves=");
		builder.append(bridgeSoldierSaves);
		builder.append(", bridgeSoldierDeaths=");
		builder.append(bridgeSoldierDeaths);
		builder.append(", bridgeSoldierEnemySaves=");
		builder.append(bridgeSoldierEnemySaves);
		builder.append(", bridgeSoldierEnemyKills=");
		builder.append(bridgeSoldierEnemyKills);
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public Team getTeam() {
		return team;
	}


	public void setTeam(Team team) {
		this.team = team;
	}


	public byte getPositionInTeam() {
		return positionInTeam;
	}


	public void setPositionInTeam(byte positionInTeam) {
		this.positionInTeam = positionInTeam;
	}


	public int getTowerHealth() {
		return towerHealth;
	}


	public void setTowerHealth(int towerHealth) {
		this.towerHealth = towerHealth;
	}


	public int getTowerEnemeyHealth() {
		return towerEnemeyHealth;
	}


	public void setTowerEnemeyHealth(int towerEnemeyHealth) {
		this.towerEnemeyHealth = towerEnemeyHealth;
	}


	public int getBridgesBuilt() {
		return bridgesBuilt;
	}


	public void setBridgesBuilt(int bridgesBuilt) {
		this.bridgesBuilt = bridgesBuilt;
	}


	public int getBridgesBuiltPoints() {
		return bridgesBuiltPoints;
	}


	public void setBridgesBuiltPoints(int bridgesBuiltPoints) {
		this.bridgesBuiltPoints = bridgesBuiltPoints;
	}


	public int getBridgesDestroyed() {
		return bridgesDestroyed;
	}


	public void setBridgesDestroyed(int bridgesDestroyed) {
		this.bridgesDestroyed = bridgesDestroyed;
	}


	public int getBridgesDestroyedPoints() {
		return bridgesDestroyedPoints;
	}


	public void setBridgesDestroyedPoints(int bridgesDestroyedPoints) {
		this.bridgesDestroyedPoints = bridgesDestroyedPoints;
	}


	public int getBridgeSoldierSaves() {
		return bridgeSoldierSaves;
	}


	public void setBridgeSoldierSaves(int bridgeSoldierSaves) {
		this.bridgeSoldierSaves = bridgeSoldierSaves;
	}


	public int getBridgeSoldierDeaths() {
		return bridgeSoldierDeaths;
	}


	public void setBridgeSoldierDeaths(int bridgeSoldierDeaths) {
		this.bridgeSoldierDeaths = bridgeSoldierDeaths;
	}


	public int getBridgeSoldierEnemySaves() {
		return bridgeSoldierEnemySaves;
	}


	public void setBridgeSoldierEnemySaves(int bridgeSoldierEnemySaves) {
		this.bridgeSoldierEnemySaves = bridgeSoldierEnemySaves;
	}


	public int getBridgeSoldierEnemyKills() {
		return bridgeSoldierEnemyKills;
	}


	public void setBridgeSoldierEnemyKills(int bridgeSoldierEnemyKills) {
		this.bridgeSoldierEnemyKills = bridgeSoldierEnemyKills;
	}
}
