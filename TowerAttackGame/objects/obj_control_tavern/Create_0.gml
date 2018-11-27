/// @description Init

enum TavernControlType {
	START = 0,
	STOP = 1
}

tavernControlType = noone;
targetTavern = instance_nearest( x, y, obj_tavern_permanent );
chargingTeam = noone;
chargeUp = 0;
colliding = false;
