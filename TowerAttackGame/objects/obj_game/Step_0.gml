/// @description Phases

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch GamePhase from " + previous + " ->  " + current );
	previousPhase = currentPhase;
}

switch( currentPhase ){
	case GamePhase.INIT:
	
		instance_create_layer( 0, 0, "lyr_gameplay", obj_countdown );
		currentPhase = GamePhase.COUNTDOWN;
	
		break;
	case GamePhase.COUNTDOWN:
		
		if( obj_countdown.currentValue <= 0 ){
			currentPhase = GamePhase.GAME_START;
		}
		
		break;
	case GamePhase.GAME_START:	
	
		instance_create_layer( 0, 0, "lyr_gameplay", obj_game_timer );
		instance_create_layer(0, 0, "lyr_gameplay", obj_spawner);

		currentPhase = GamePhase.GAME;			
	
		break;
	case GamePhase.GAME:
	
		if( obj_game_timer.currentValue <= 0 ){
			trace( "GAME END BY TIMER" );
			currentPhase = GamePhase.GAME_END;
			return;
		}
		
		if( obj_tower_red.towerHealth <= 0 ){
			trace( "GAME END BY RED TOWER HEALTH" );
			currentPhase = GamePhase.GAME_END;
			return;			
		}
		
		if( obj_tower_blue.towerHealth <= 0 ){
			trace( "GAME END BY BLUE TOWER HEALTH" );
			currentPhase = GamePhase.GAME_END;
			return;			
		}
	
	
		break;
	case GamePhase.GAME_END:
	
		if( !instance_exists( obj_soldier ) ){
			room_goto_previous();
		}
	
		break;
}