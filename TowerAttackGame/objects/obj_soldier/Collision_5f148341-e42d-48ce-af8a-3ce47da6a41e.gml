/// @description  Start a fight

if( team == other.team ){
	// Take into account only opponents
	return;	
}

// Trigger fight only if both of them are not fighting
if( currentPhase == SoldierPhase.FOLLOW_PATH && other.currentPhase == SoldierPhase.FOLLOW_PATH ){
	currentPhase = SoldierPhase.INIT_FIGHT;
	soldierToFight = other;
	other.currentPhase = SoldierPhase.INIT_FIGHT;
	other.soldierToFight = self;
}

if( currentPhase == SoldierPhase.CHARGE_TO_FIGHT && other.currentPhase == SoldierPhase.CHARGE_TO_FIGHT ){
	currentPhase = SoldierPhase.INIT_FIGHT;
	soldierToFight = other;
	other.currentPhase = SoldierPhase.INIT_FIGHT;
	other.soldierToFight = self;
}