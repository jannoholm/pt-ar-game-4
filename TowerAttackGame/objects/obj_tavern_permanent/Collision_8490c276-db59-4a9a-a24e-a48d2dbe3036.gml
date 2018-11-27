/// @description Grab soldier to tavern

if ( activated && other.tavernCrossed == false ) {
	other.currentPhase = SoldierPhase.GO_TO_TAVERN;
}

// In any case don't check for further tavern collision checks
other.tavernCrossed = true;

