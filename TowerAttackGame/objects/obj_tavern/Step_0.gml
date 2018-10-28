/// @description Countdown rest

if( obj_game.currentPhase == GamePhase.GAME_END ){

}


// Update all soldiers count down and if it reaches zero, trigger spawn from tavern
for( var soldier = ds_map_find_first( soldiersInTavern ); !is_undefined( soldier ); soldier = ds_map_find_next( soldiersInTavern, soldier ) ) {

	var currentCounter = soldiersInTavern[? soldier];
	
	// Apply the counter check rule only once!
	if( currentCounter == 0 ) {
		// Soldiers are spawned from tavern once per frame
		ds_list_add( soldiersToSpawn, soldier );
		with( soldier ){
			show_debug_message("Added soldier to spawn, id=" + string( spawnIndex ) );
		}
	} else if( obj_game.currentPhase == GamePhase.GAME_END ){
		// If game has ended, then spawn them all anyway
		ds_list_add( soldiersToSpawn, soldier );
	}
	
	ds_map_set( soldiersInTavern, soldier, currentCounter - 1 );	
}

//for( var i = 0; i < size; i++ ) {
	
//	var currentCounter = ds_map_find_value( soldiersInTavern, currentSoldier );
	
//	// Apply the counter check rule only once!
//	if( currentCounter == 0 ) {
//		// Soldiers are spawned from tavern once per frame
//		ds_list_add( soldiersToSpawn, currentSoldier );
//		//show_debug_message("Added soldier to spawn, id=" + string( currentSoldier.spawnIndex ) + ", spawn list size=" + string( ds_list_size( soldiersToSpawn ) ) );
//	} else {
//		ds_map_set( soldiersInTavern, currentSoldier, currentCounter - 1 );	
//	}
	
//	currentSoldier = ds_map_find_next( soldiersInTavern, currentSoldier );
//}

var size = ds_list_size( soldiersToSpawn );
var soldierToRemove = undefined;


// For some reason, the soldiers do not end up at the position 0 in some edge-case scenario
// Scan over the list until first soldier object
for( var i = 0; i < size; i++ ) {
	
	soldierToRemove = ds_list_find_value( soldiersToSpawn, i );
	
	// If a soldier was found, then remove from the list and trigger spawn
	if( !is_undefined(soldierToRemove) ) {
		
		ds_list_delete( soldiersToSpawn, i );
		
		with( soldierToRemove ) {
			show_debug_message ("Found soldier to spawn, id=" + string( soldierToRemove ) );
			ds_map_delete( other.soldiersInTavern, self );
			currentPhase = SoldierPhase.SPAWN_FROM_TAVERN;
		}
		
		
		break;
	} else {
		show_debug_message("No soldier found on position=" + string( i ) + ", !undifined=" + string( is_undefined( soldierToRemove ) ) + ", exists=" + string( instance_exists( soldierToRemove ) ) );
	}
}
