/// @description Insert description here
// You can write your code in this editor

if( obj_game.currentPhase != GamePhase.GAME && obj_game.currentPhase != GamePhase.DEMO ){
	// No actions if game is not in progress
	chargeUp = 0;
	colliding = 0;
	return;	
}

// #TODO: Take into account the points to be recieved during charge
if( !targetTavern.protected && colliding > 0 && chargingTeam.actionPoints > 10 ) {
	// Shield is down, start counting
	
	if( tavernControlType == TavernControlType.START && targetTavern.activated ){
		// Tavern is already started, don't start it again
	} else if( tavernControlType == TavernControlType.STOP && !targetTavern.activated ){
		// Tavern is already stopped, don't stop it again
	} else {
		chargeUp++;	
	}	
	
	if( chargeUp > 2 * room_speed ) {
		show_debug_message( "CHARGE COMPLETE!" );
		chargingTeam.actionPoints -= 10;
		chargeUp = 0;
		with( targetTavern ) {
			activatedByTeam = other.chargingTeam;
			activated = !activated; // Reverse the status
		}
	}
} else {
	// If tavern went under protection by another team action
	chargeUp = 0;
}

// Clear the collision mark for the next frame
colliding--;
