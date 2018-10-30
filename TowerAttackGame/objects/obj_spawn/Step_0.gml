/// @description Spawn phases

if( ds_list_empty( spawnTasks ) || !spawnDelayCleared ){
	// If no tasks or delay is not clear, do nothing
	return;
}

var task = ds_list_find_value( spawnTasks, 0 );
ds_list_delete( spawnTasks, 0 );
alarm_set( spawnDelayAlarmIndx, spawnDelay );
spawnDelayCleared = false;

// Actual spawn implementation
with( instance_create_layer(-100, -100, "lyr_soldiers", task.soldier) ){
	team = other.team;
	path = other.path;
	pathDirection = other.pathDirection;
	pathStartPosition = other.pathStartPosition;
}
