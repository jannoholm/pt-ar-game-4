/// @description Fix or break bridge

shield = 10 * room_speed;

if( durability > 0 ) {
	show_debug_message("BREAKING BRIDGE");
	
	activatedByTeam.bridgesDestroyed++;
	activatedByTeam.bridgesDestroyedPoints += durability;
	
	durability = 0;
	fillUp = 0;
} else {
	show_debug_message("FIXING BRIDGE");
	
	activatedByTeam.bridgesBuilt++;
	activatedByTeam.bridgesBuiltPoints += fillUp;
	
	durability = 100;
	fillUp = 0;
}
