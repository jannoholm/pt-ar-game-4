/// @description Phases

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch LobbyPhase from " + previous + " ->  " + current );
	previousPhase = currentPhase;
}


switch( currentPhase ){
	case LobbyPhase.INIT:
	
		currentPhase = LobbyPhase.WAITING_PLAYERS;
	
		break;
	case LobbyPhase.WAITING_PLAYERS:
	
		if( 
			playerRedOne != noone && 
			playerRedTwo != noone && 
			playerBlueOne != noone && 
			playerBlueTwo != noone ){
			
			trace( "All players filled" );
			currentPhase = LobbyPhase.PLAYERS_FULL;
			
		}
	
		
		break;
	case LobbyPhase.PLAYERS_FULL:
	
		obj_tower_attack.currentPhase = MainPhase.GAME_INIT;
	
		break;
}