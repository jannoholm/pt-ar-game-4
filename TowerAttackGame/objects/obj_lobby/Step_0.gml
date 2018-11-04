/// @description Phases

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch LobbyPhase from " + previous + " ->  " + current );
	previousPhase = currentPhase;
}


switch( currentPhase ){
	case LobbyPhase.INIT:
	
		instance_destroy(playerOneTop);
		instance_destroy(playerTwoTop);
		playerOneTop = noone;
		playerTwoTop = noone;

		currentPhase = LobbyPhase.WAITING_PLAYERS;
	
		break;
	case LobbyPhase.WAITING_PLAYERS:
	
		if( 
			playerOneTop != noone && 
			playerOneBottom != noone && 
			playerTwoTop != noone && 
			playerTwoBottom != noone ){
			
			trace( "All players filled" );
			currentPhase = LobbyPhase.PLAYERS_FULL;
			
		}
	
		
		break;
	case LobbyPhase.PLAYERS_FULL:
	
		obj_tower_attack.currentPhase = MainPhase.GAME_INIT;
		currentPhase = LobbyPhase.COMPLETE;
	
		break;
	case LobbyPhase.COMPLETE:
	
		break;
}