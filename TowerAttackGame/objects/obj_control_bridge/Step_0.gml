/// @description Insert description here
// You can write your code in this editor

if( obj_game.currentPhase != GamePhase.GAME ){
	// No actions if game is not in progress
	chargeUp = 0;
	colliding = false;
	return;	
}

// #TODO: Take into account the points to be recieved during charge
if( targetBridge.shield <= 0 && player.actionPoints > 10 ) {
	// Shield is down start counting
	if( colliding ){
		chargeUp++;	
	} else {
		chargeUp = 0;
	}
	
	if( chargeUp > 2 * room_speed ) {
		show_debug_message("CHARGE COMPLETE!");
		player.actionPoints -= 10;
		chargeUp = 0;
		with( targetBridge ) {
			alarm_set( fixOrBreakAlarmIndx, 1 );
		}
	}
} else {
	// If bridge went under protection by another player action
	chargeUp = 0;
}


// Clear the collision mark for the next frame
colliding = false;
