/// @description Fix bridge

protected = true; // Shield bar will pick up the value and start countdown

show_debug_message("FIXING BRIDGE");

activatedByTeam.bridgesBuilt++;
activatedByTeam.bridgesBuiltPoints += fillUp;

durability = 100;
fillUp = 0;

audio_play_sound( snd_bridge_built, 10, false );
	