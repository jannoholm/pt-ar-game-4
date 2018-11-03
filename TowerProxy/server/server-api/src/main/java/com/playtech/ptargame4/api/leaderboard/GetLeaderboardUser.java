package com.playtech.ptargame4.api.leaderboard;


import com.playtech.ptargame.common.util.StringUtil;

import java.nio.ByteBuffer;

public class GetLeaderboardUser {

    private int userId;
    private String name;
    private int eloRating;
    private int matches;
    private int towerHealth;
    private int enemyTowerHealth;
    private int totalScore;
    private int position;

    public void parse(ByteBuffer messageData) {
        // make sure we read our needed bytes and all our expected bytes
        int byteCount = messageData.getInt();
        byte[] bytes = new byte[byteCount];
        messageData.get(bytes);
        messageData = ByteBuffer.wrap(bytes).order(messageData.order());

        // read structure data
        userId=messageData.getInt();
        name=StringUtil.readUTF8String(messageData);
        eloRating=messageData.getInt();
        matches=messageData.getInt();
        towerHealth=messageData.getInt();
        enemyTowerHealth =messageData.getInt();
        totalScore=messageData.getInt();
        position=messageData.getInt();
    }

    public void format(ByteBuffer messageData) {
        // remember old position
        int position = messageData.position();
        // reserve space, but set some arbitrary value
        messageData.putInt(0);

        messageData.putInt(userId);
        StringUtil.writeUTF8String(name, messageData);
        messageData.putInt(eloRating);
        messageData.putInt(matches);
        messageData.putInt(towerHealth);
        messageData.putInt(enemyTowerHealth);
        messageData.putInt(totalScore);
        messageData.putInt(position);

        // fix length
        messageData.putInt(position, messageData.position()-position-4);
    }

    protected void toStringImpl(StringBuilder s) {
        s.append("id=").append(getUserId());
        s.append(", name=").append(getName());
        s.append(", eloRating=").append(getEloRating());
        s.append(", matches=").append(getMatches());
        s.append(", towerHealth=").append(getTowerHealth());
        s.append(", enemyTowerHealth=").append(getEnemyTowerHealth());
        s.append(", totalScore=").append(getTotalScore());
        s.append(", position=").append(getPosition());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEloRating() {
        return eloRating;
    }

    public void setEloRating(int eloRating) {
        this.eloRating = eloRating;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getTowerHealth() {
        return towerHealth;
    }

    public void setTowerHealth(int towerHealth) {
        this.towerHealth = towerHealth;
    }

    public int getEnemyTowerHealth() {
        return enemyTowerHealth;
    }

    public void setEnemyTowerHealth(int enemyTowerHealth) {
        this.enemyTowerHealth = enemyTowerHealth;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
