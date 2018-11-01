/// @description Fill or erode bridge

if ( durability > 0 ) {
	
	// Roadblock is fixed, degrade if soldier passes

	var indx = ds_list_find_index(other.bridgesCrossed, self);

	if ( indx == -1 ) {
		// First time hitbox collision
		durability = durability - 10;
		ds_list_add(other.bridgesCrossed, self);
		
		if( durability == 0 ){
			// Bridge broken by natural causes, reset activation if any
			activatedByTeam = noone;
		}
		
		if( activatedByTeam != noone && activatedByTeam == other.team ){
			// If bridge was fixed by the same team as the soldier, give good points
			activatedByTeam.bridgeSoldierSaves += 1;
		} else if( activatedByTeam !=noone && activatedByTeam != other.team ){
			// If bridge was fixed by a team but enemy soldier uses it, give bad points
			activatedByTeam.bridgeSoldierEnemySaves += 1;
		} 
	}

} else {

	// Roadblock is active, fill it up with a body
	fillUp = fillUp + 1;
	
	if( activatedByTeam != noone && activatedByTeam != other.team ){
		// If bridge was destroyed by a team and enemy soldier uses it, give good points
		activatedByTeam.bridgeSoldierEnemyKills += 1;
	} else if( activatedByTeam !=noone && activatedByTeam == other.team ){
		// If bridge was destroyed by a team but own soldier uses it, give bad points
		activatedByTeam.bridgeSoldierDeaths += 1;
	} 

	// Destory the colliding soldior
	// #TODO: Play animation
	instance_destroy(other);
}