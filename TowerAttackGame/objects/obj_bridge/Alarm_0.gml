/// @description Fix or break bridge

protected = true; // Shield bar will pick up the value and start countdown

if( durability > 0 ) {
	show_debug_message("BREAKING BRIDGE");
	
	activatedByTeam.bridgesDestroyed++;
	activatedByTeam.bridgesDestroyedPoints += durability;
	
	durability = 0;
	fillUp = 0;
	
	audio_play_sound( snd_bridge_destroyed, 10, false );
	
} else {
	show_debug_message("FIXING BRIDGE");
	
	activatedByTeam.bridgesBuilt++;
	activatedByTeam.bridgesBuiltPoints += fillUp;
	
	durability = 100;
	fillUp = 0;
	
	audio_play_sound( snd_bridge_built, 10, false );
}
