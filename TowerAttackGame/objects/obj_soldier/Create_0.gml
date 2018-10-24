/// @description Insert description here

enum STATE_SOLDIER {
	SPAWN,
	FOLLOW_PATH,
	FIGHT,
	CHARGE_TO_FIGHT,
	GO_TO_TAVERN,
	IN_TAVERN,
	SPAWN_FROM_TAVERN,
	ENTER_TUNNEL,
	IN_TUNNEL,
	EXIT_TUNNEL
}

currentState = STATE_SOLDIER.SPAWN;

// Global parameter to track "age" of the soldier
spawnIndex = global.soldierSpawnIndex++;

team = noone;

unitDeadAlarmIndx = 0;

pathMoveSpeed = 2; // Same for all soldiers

path = noone; // Set by child objects
pathDirection = noone; // Set by child objects, 1 from left to right; -1 from right to left
pathStartPosition = noone; // Set by child objects

fightingSprite = noone; // Set by child objects

// Bridge cross detection - hitbox only once - contains list of already hit brdiges
bridgesCrossed = ds_list_create();
tavernCrossed = false;
chargedSoldier = noone;