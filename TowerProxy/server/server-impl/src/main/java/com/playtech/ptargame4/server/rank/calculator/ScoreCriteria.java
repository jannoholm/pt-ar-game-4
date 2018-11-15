package com.playtech.ptargame4.server.rank.calculator;

public enum ScoreCriteria {
	TOWER_HEALTH("towerHealth", 0.5, 500),
	BRIDGES_BUILT("bridgesBuilt", 0.1, 5),
    BRIDGES_DESTROYED("bridgesDestroyed", 0.1, 5),
    BRIDGE_SOLDIER_ENEMY_KILLS("enemyKills", 0.1, 20),
    BRIDGE_SOLDIER_SAVES("soldierSaves", 0.1, 20),
    BRIDGE_SOLDIER_DEATH("soldierDeaths", 0.1, 10),
    BRIDGE_SOLDIER_ENEMY_SAVES("enemySaves", 0.1, -10),;

	private final String key;
	private final double value;
	private final long pointValue;

	ScoreCriteria(String key, double value, long pointValue) {
		this.key = key;
		this.value = value;
		this.pointValue = pointValue;
	}

	public String getKey() {
		return key;
	}

	public double getValue() {
		return value;
	}

	public long getPointValue() {
		return pointValue;
	}

}
