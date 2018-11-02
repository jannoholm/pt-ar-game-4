/// @description Insert description here
// You can write your code in this editor

// Inherit the parent event
event_inherited();

if( sprite_index == unitSlashAnimation ){
	var prevPathPosition = path_positionprevious;
	path_start(path, pathDirection * pathMoveSpeed, path_action_stop, true);
	path_position = prevPathPosition;

	currentPhase = SoldierPhase.FOLLOW_PATH;

	sprite_index = unitWalkAnimation;
	image_index = 0;
}