/// @description Grab soldier to tavern

if ( other.tavernCrossed == false && irandom(99) < 50 ) {
	// Minimum time has to be the time for soldier to reach the tabern for simplicity sake
	var timeToRest = irandom_range(room_speed*10, room_speed*20);
	ds_map_add( soldiersInTavern, other, timeToRest );
	other.currentPhase = SoldierPhase.GO_TO_TAVERN;
	
	show_debug_message("Added soldier with delay= " + string( timeToRest ) + ", total=" + string( ds_map_size( soldiersInTavern ) ) );
}

// In any case don't check for further tavern collision checks
other.tavernCrossed = true;

