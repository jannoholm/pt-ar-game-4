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

		currentPhase = MainPhase.LOBBY
		room_goto_next();
		
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
	
		
		lastGameScore = instance_create_layer( 0, 0, "lyr_tower_attack", obj_score_keeper );
		lastGameScore.redTeam = instance_create_layer( 0, 0, "lyr_tower_attack", obj_score_team );
		lastGameScore.blueTeam = instance_create_layer( 0, 0, "lyr_tower_attack", obj_score_team );
		
		if( obj_game.redTower.towerHealth > obj_game.blueTower.towerHealth ){
			lastGameScore.winnerTeam = WinnerTeam.RED;
		} else if( obj_game.redTower.towerHealth < obj_game.blueTower.towerHealth ){
			lastGameScore.winnerTeam = WinnerTeam.BLUE;
		} else {
			lastGameScore.winnerTeam = WinnerTeam.DRAW;
		}
		
		lastGameScore.gameTime = obj_game_timer.currentValue;
		
		lastGameScore.redTowerHealth = obj_game.redTower.towerHealth;
		lastGameScore.blueTowerHealth = obj_game.blueTower.towerHealth;

		// TODO: Loop over all players and create list instead
		var team = obj_game.redTeam;
		with( lastGameScore.redTeam ){
			teamId = team.teamId;
			bridgesBuilt = team.bridgesBuilt;
			bridgesBuiltPoints = team.bridgesBuiltPoints;
			bridgesDestroyed = team.bridgesDestroyed;
			bridgesDestroyedPoints = team.bridgesDestroyedPoints;
			bridgeSoldierSaves = team.bridgeSoldierSaves;
			bridgeSoldierDeaths = team.bridgeSoldierDeaths;
			bridgeSoldierEnemySaves = team.bridgeSoldierEnemySaves;
			bridgeSoldierEnemyKills = team.bridgeSoldierEnemyKills;
		}
		team = obj_game.blueTeam;
		with( lastGameScore.blueTeam ){
			teamId = team.teamId;
			bridgesBuilt = team.bridgesBuilt;
			bridgesBuiltPoints = team.bridgesBuiltPoints;
			bridgesDestroyed = team.bridgesDestroyed;
			bridgesDestroyedPoints = team.bridgesDestroyedPoints;
			bridgeSoldierSaves = team.bridgeSoldierSaves;
			bridgeSoldierDeaths = team.bridgeSoldierDeaths;
			bridgeSoldierEnemySaves = team.bridgeSoldierEnemySaves;
			bridgeSoldierEnemyKills = team.bridgeSoldierEnemyKills;
		}
		
		scr_send_game_results_store_request( lastGameScore );
		
		currentPhase = MainPhase.GAME_RESULT
	
		break;
	case MainPhase.GAME_RESULT:
	
		currentPhase = MainPhase.LOBBY
		room_goto_previous();
	
		break;
}