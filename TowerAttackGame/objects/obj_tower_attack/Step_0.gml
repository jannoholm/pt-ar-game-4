/// @description Insert description here
// You can write your code in this editor

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch MainPhase from " + previous + " ->  " + current );
	previousPhase = currentPhase;
}

switch( currentPhase ){
	case MainPhase.INIT:
	
		break;
	case MainPhase.GO_TO_LOBBY:
	
		room_goto_next();
		currentPhase = MainPhase.LOBBY
		
		break;
	case MainPhase.LOBBY:
	
		break;
	case MainPhase.GAME_INIT:
	
		currentPhase = MainPhase.GAME;
		room_goto_next();
	
		break;
	case MainPhase.GAME:
	
		break;
	case MainPhase.GAME_END:
	
		break;
	case MainPhase.GAME_RESULT:
	
		break;
}