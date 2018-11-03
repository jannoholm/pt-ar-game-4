package com.playtech.ptargame4.server.rank.calculator;

public enum ScoreCriteria {
	// TODO: Update to tower attack
	TOWER_HEALTH("goal", 0.5, 500), BRIDGES_BUILT("ballHit", 0.1, 10), BRIDGES_DESTROYED("bulletHit", 0.2, 100), BRIDGE_SOLDIER_ENEMY_KILLS("boostHit", 0.2,
			20);

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
