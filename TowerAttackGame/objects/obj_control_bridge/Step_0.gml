/// @description Insert description here
// You can write your code in this editor

if( obj_game.currentPhase != GamePhase.GAME && obj_game.currentPhase != GamePhase.DEMO ){
	// No actions if game is not in progress
	chargeUp = 0;
	colliding = 0;
	if( buildSoundIndex != noone ){
		audio_stop_sound( buildSoundIndex );
	}
	buildSoundIndex = noone;
	return;	
}

// #TODO: Take into account the points to be recieved during charge
if( !targetBridge.protected && colliding > 0 && chargingTeam.actionPoints > 10 ) {
	// Shield is down start counting and bridge is up
	if( bridgeControlType == BridgeControlType.DESTROY && targetBridge.durability <= 0 ){
		// No charge up, bridge is destroyed, don't destory it again
	} else {
		if( buildSoundIndex == noone ){
			buildSoundIndex = audio_play_sound( snd_bulding_bridge, 3, false );
			audio_sound_gain( buildSoundIndex, 0.5, 0 );
		}
		chargeUp++;	
	}	
	
	if( chargeUp > 1 * room_speed ) {
		show_debug_message( "CHARGE COMPLETE!" );
		chargingTeam.actionPoints -= 10;
		chargeUp = 0;
		if( buildSoundIndex != noone ){
			audio_stop_sound( buildSoundIndex );
		}
		buildSoundIndex = noone;
		with( targetBridge ) {
			activatedByTeam = other.chargingTeam;
			event_user( other.bridgeControlType );
		}
	}
} else {
	// If bridge went under protection by another team action
	chargeUp = 0;
	if( buildSoundIndex != noone ){
		audio_stop_sound( buildSoundIndex );
	}
	buildSoundIndex = noone;
}


// Cooldown the collision - allow small "twitching" of the avatar
colliding--;
