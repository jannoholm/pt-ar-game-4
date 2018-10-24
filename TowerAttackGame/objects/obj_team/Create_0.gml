/// @description Insert description here

enum TeamId {
	RED = 0,
	BLUE = 1
}

enum Position {
	TOP = 0,
	BOTTOM = 1
}

teamId = noone;

actionPoints = 10;
actionTokenSpawn = 0;

// Initiallize action point recurring collection alarm
alarm[0] = room_speed;

bridgesBuilt = 0;
bridgesBuiltPoints = 0;
bridgesDestroyed = 0;
bridgesDestroyedPoints = 0;
bridgeSoldierSaves = 0;
bridgeSoldierDeaths = 0;
bridgeSoldierEnemySaves = 0;
bridgeSoldierEnemyKills = 0;