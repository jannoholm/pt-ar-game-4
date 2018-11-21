/// @description Destroy bridge

protected = true; // Shield bar will pick up the value and start countdown

show_debug_message("BREAKING BRIDGE");

activatedByTeam.bridgesDestroyed++;
activatedByTeam.bridgesDestroyedPoints += durability;

durability = 0;
fillUp = 0;

audio_play_sound( snd_bridge_destroyed, 10, false );