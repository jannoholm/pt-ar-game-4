/// @description Insert description here

// Loop over all spawns and check for collision
with( obj_spawn ) {

	if( other.activatedSpawnLocation != self && place_meeting( x, y, other) ){
		
		// Switch the active location
		if( other.player.actionPoints >= 3 ) {
			
			if( other.activatedSpawnLocation != noone ){
				// Reduce the activation level of previous location
				other.activatedSpawnLocation.activationLevel--;
			}
			
			// Decrease action points
			other.player.actionPoints -= 3;
			// Replace new location
			other.activatedSpawnLocation = self;
			// Increase activation level of new loaction
			activationLevel++;
			// TODO: Take min of new level and next level activation time to stop abuse
			spawnDelayCleared = true;
		}
	}
}