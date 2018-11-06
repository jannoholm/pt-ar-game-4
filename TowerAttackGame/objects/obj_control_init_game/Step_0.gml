/// @description Insert description here
// You can write your code in this editor

var previousState = activated;

if( obj_game.currentPhase != GamePhase.WAIT_PLAYERS_READY ){
	// No actions if game is not in progress
	colliding = false;
	activated = false;
	visible = false;
} else {

	if( colliding ){
		activated = true;	
	} else {
		activated = false;
	}

	// Clear the collision mark for the next frame
	colliding = false;	
}

if( previousState != activated ){
	trace( "Init game control area changed status " + string( previousState ) + "->" + string( activated ) );
}