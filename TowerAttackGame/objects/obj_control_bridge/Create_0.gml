/// @description Insert description here
// You can write your code in this editor

enum BridgeControlType {
	BUILD = 0,
	DESTROY = 1
}

bridgeControlType = noone;
targetBridge = instance_nearest(x, y, obj_bridge);
chargingTeam = noone;
chargeUp = 0;
colliding = 0;

buildSoundIndex = noone;