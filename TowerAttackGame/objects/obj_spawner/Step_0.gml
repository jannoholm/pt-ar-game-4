/// @description Spawn soldiers in random spawns

if( obj_game.currentPhase != GamePhase.GAME ){
	// No game, no soldiers
	return;	
}

if( soldiersSpawnDelayCounter-- > 0 ){
	// Delay not reached yet
	return;
}

soldiersSpawnDelayCounter = max( room_speed * 2, room_speed * (soldiersSpawnDelay--) );
soldiersToSpawn = min( 6, soldiersToSpawn+1 ); 

// TODO: Choose randomly
var spawnForRed = noone; 
var spawnForBlue = noone; ;

var r = irandom(100);
if( r < 15 ) {
	spawnForRed = obj_soldier_red_top;
} else if( r < 45 ) {
	spawnForRed = obj_soldier_red_middle_high;
} else if( r < 75 ) {
	spawnForRed = obj_soldier_red_middle_low;
} else {
	spawnForRed = obj_soldier_red_bottom;
}

var r = irandom(100);
if( r < 15 ) {
	spawnForBlue = obj_soldier_blue_top;
} else if( r < 45 ) {
	spawnForBlue = obj_soldier_blue_middle_high;
} else if( r < 75 ) {
	spawnForBlue = obj_soldier_blue_middle_low;
} else {
	spawnForBlue = obj_soldier_blue_bottom;
}

// Loop over all spawns
with( obj_spawn ){
	
	if( soldierToSpawn == spawnForRed || soldierToSpawn == spawnForBlue ) {
		soldiersToSpawn = other.soldiersToSpawn;
		alarm_set( spawnGroupAlarmIndx, 1 ); // Next frame	
	}
	
	show_debug_message("Soldier index=" + string(other.soldiersSpawnDelay) + ", other.soldiersToSpawn=" + string(other.soldiersToSpawn) + ", soldiersToSpawn=" + string(soldiersToSpawn) );
}

	