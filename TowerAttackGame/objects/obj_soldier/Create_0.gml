/// @description Init

// STATE MACHINE START
enum SoldierPhase {
	SPAWN,
	FOLLOW_PATH,
	INIT_FIGHT,
	FIGHT,
	CHARGE_TO_FIGHT,
	GO_TO_TAVERN,
	IN_TAVERN,
	SPAWN_FROM_TAVERN,
	ENTER_TUNNEL,
	IN_TUNNEL,
	EXIT_TUNNEL,
	DEAD
}

phases = ds_list_create();
ds_list_add(phases,
	"SPAWN",
	"FOLLOW_PATH",
	"INIT_FIGHT",
	"FIGHT",
	"CHARGE_TO_FIGHT",
	"GO_TO_TAVERN",
	"IN_TAVERN",
	"SPAWN_FROM_TAVERN",
	"ENTER_TUNNEL",
	"IN_TUNNEL",
	"EXIT_TUNNEL",
	"DEAD"
)

previousPhase = SoldierPhase.SPAWN;
currentPhase = SoldierPhase.SPAWN;
// STATE MACHINE END

enum SoldierType {
	BASIC,
	BOMBER,
	ELITE
}

enum FightUnitEvent {
	BASIC,
	BOMBER,
	ELITE,
	BURN_BY_BOMBER
}

// Global parameter to track "age" of the soldier
spawnIndex = global.soldierSpawnIndex++;

type = noone;
team = noone;

unitFightAnimation = noone;
unitDeadAnimation = noone;

path = noone; // Set by spawner
pathMoveSpeed = 5; // Base speed
pathDirection = noone; // Set by child objects, 1 from left to right; -1 from right to left
pathStartPosition = noone; // Set by child objects

image_xscale = -1;

fightingSprite = noone; // Set by child objects

// Bridge cross detection - hitbox only once - contains list of already hit brdiges
bridgesCrossed = ds_list_create();
tavernCrossed = false;
soldierInFront = noone; // For speed limitation on path
soldierToFight = noone;
chargedSoldier = noone;

damageToTower = 1;