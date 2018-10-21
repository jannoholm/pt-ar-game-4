/// @description Start a fight

// Trigger fight only if both of them are not fighting
if( currentState == STATE_SOLDIER.FOLLOW_PATH && other.currentState == STATE_SOLDIER.FOLLOW_PATH ){
	currentState = STATE_SOLDIER.FIGHT;
	other.currentState = STATE_SOLDIER.FIGHT;
}

if( currentState == STATE_SOLDIER.CHARGE_TO_FIGHT && other.currentState == STATE_SOLDIER.CHARGE_TO_FIGHT ){
	currentState = STATE_SOLDIER.FIGHT;
	other.currentState = STATE_SOLDIER.FIGHT;
}