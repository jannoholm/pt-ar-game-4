/// @description Spawn 5 soldiers

if( soldiersToSpawn > 0 ) {
	// show_debug_message("Spawinng soldier, soldiersToSpawn=" + string(soldiersToSpawn));
	instance_create_layer(-10, -10, "lyr_soldiers", soldierToSpawn);	
	alarm[1] = room_speed / 4;
	soldiersToSpawn -= 1;
}