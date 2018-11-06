/// @description Grab soldier to tavern

if ( activated && other.tavernCrossed == false && irandom(99) < 100 ) { // #TODO: Make configurable
	// Minimum time has to be the time for soldier to reach the tabern for simplicity sake
	other.currentPhase = SoldierPhase.GO_TO_TAVERN;
	
	//if( activatedByTeam != noone && activatedByTeam != other.team ){
	//	// If bridge was destroyed by a team and enemy soldier uses it, give good points
	//	activatedByTeam.bridgeSoldierEnemyKills += 1;
	//} else if( activatedByTeam !=noone && activatedByTeam == other.team ){
	//	// If bridge was destroyed by a team but own soldier uses it, give bad points
	//	activatedByTeam.bridgeSoldierDeaths += 1;
	//}
}

// In any case don't check for further tavern collision checks
other.tavernCrossed = true;

