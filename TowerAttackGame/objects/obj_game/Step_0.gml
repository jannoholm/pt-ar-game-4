/// @description Phases

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch GamePhase from " + previous + " ->  " + current );
	previousPhase = currentPhase;
}

switch( currentPhase ){
	case GamePhase.INIT:
	
		if( obj_tower_attack.firstTimeStart ){
			// Fast forward to demo mode
			currentPhase = GamePhase.GAME_START;
		} else {
			currentPhase = GamePhase.WAIT_PLAYERS_READY;
		}
	
		break;
	case GamePhase.WAIT_PLAYERS_READY:
		
		if( obj_control_init_game_team_one.activated && obj_control_init_game_team_two.activated  ){
			instance_create_layer( 0, 0, "lyr_gameplay", obj_countdown );
			currentPhase = GamePhase.COUNTDOWN;	
		}
		
		
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
	
		if( obj_tower_attack.firstTimeStart ){
			// Fast forward to demo mode
			currentPhase = GamePhase.GAME_END;
		}
	
		if( obj_game_timer.currentValue <= 0 ){
			trace( "GAME END BY TIMER" );
			currentPhase = GamePhase.GAME_END;
			return;
		}
		
		if( obj_tower_team_one.towerHealth <= 0 ){
			trace( "GAME END BY RED TOWER HEALTH" );
			currentPhase = GamePhase.GAME_END;
			return;			
		}
		
		if( obj_tower_team_two.towerHealth <= 0 ){
			trace( "GAME END BY BLUE TOWER HEALTH" );
			currentPhase = GamePhase.GAME_END;
			return;			
		}
	
	
		break;
	case GamePhase.GAME_END:
		
		with( obj_tower_attack ){
			// Switch to next phase only once
			if( currentPhase == MainPhase.GAME ){
				currentPhase = MainPhase.GAME_END;
			} else if ( currentPhase == MainPhase.LOBBY ){
				// Fast forward to demo mode if first time
				currentPhase = MainPhase.DEMO;
				firstTimeStart = false;
			}
		}
		
		currentPhase = GamePhase.DEMO;
		
		with( obj_bridge ){
			durability = 100;
		}
	
		break;
	case GamePhase.DEMO:
	
		break;
}