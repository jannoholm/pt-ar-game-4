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
		
		lastGameScore = instance_create_layer( 0, 0, "lyr_tower_attack", obj_score_keeper );
		lastGameScore.teamOne = instance_create_layer( 0, 0, "lyr_tower_attack", obj_score_team );
		lastGameScore.teamTwo = instance_create_layer( 0, 0, "lyr_tower_attack", obj_score_team );
		
		lastGameScore.teamOne.playerId = obj_lobby.playerOneTop.playerId;
		lastGameScore.teamTwo.playerId = obj_lobby.playerTwoTop.playerId;
		
		lastGameScore.teamOne.playerName = obj_lobby.playerOneTop.playerName;
		lastGameScore.teamTwo.playerName = obj_lobby.playerTwoTop.playerName;
	
		currentPhase = MainPhase.GAME;
		
		room_restart();
	
		break;
	case MainPhase.GAME:
	
		break;
	case MainPhase.GAME_END:
			
		if( obj_game.teamOneTower.towerHealth > obj_game.teamTwoTower.towerHealth ){
			lastGameScore.winnerTeam = WinnerTeam.ONE;
		} else if( obj_game.teamOneTower.towerHealth < obj_game.teamTwoTower.towerHealth ){
			lastGameScore.winnerTeam = WinnerTeam.TWO;
		} else {
			lastGameScore.winnerTeam = WinnerTeam.DRAW;
		}
		
		lastGameScore.gameTime = obj_game_timer.currentValue;
		
		lastGameScore.teamOneTowerHealth = obj_game.teamOneTower.towerHealth;
		lastGameScore.teamTwoTowerHealth = obj_game.teamTwoTower.towerHealth;

		// TODO: Loop over all players and create list instead
		var team = obj_game.teamOne;
		with( lastGameScore.teamOne ){
			teamId = team.teamId;
			bridgesBuilt = team.bridgesBuilt;
			bridgesBuiltPoints = team.bridgesBuiltPoints;
			bridgesDestroyed = team.bridgesDestroyed;
			bridgesDestroyedPoints = team.bridgesDestroyedPoints;
			bridgeSoldierSaves = team.bridgeSoldierSaves;
			bridgeSoldierDeaths = team.bridgeSoldierDeaths;
			bridgeSoldierEnemySaves = team.bridgeSoldierEnemySaves;
			bridgeSoldierEnemyKills = team.bridgeSoldierEnemyKills;
			
			//tavernSoldierKills = team.tavernSoldierKills;
			//tavernSoldierDeaths = team.tavernSoldierDeaths;
		}
		team = obj_game.teamTwo;
		with( lastGameScore.teamTwo ){
			teamId = team.teamId;
			bridgesBuilt = team.bridgesBuilt;
			bridgesBuiltPoints = team.bridgesBuiltPoints;
			bridgesDestroyed = team.bridgesDestroyed;
			bridgesDestroyedPoints = team.bridgesDestroyedPoints;
			bridgeSoldierSaves = team.bridgeSoldierSaves;
			bridgeSoldierDeaths = team.bridgeSoldierDeaths;
			bridgeSoldierEnemySaves = team.bridgeSoldierEnemySaves;
			bridgeSoldierEnemyKills = team.bridgeSoldierEnemyKills;
			
			//tavernSoldierKills = team.tavernSoldierKills;
			//tavernSoldierDeaths = team.tavernSoldierDeaths;
		}
		
		scr_send_game_results_store_request( lastGameScore );
		
		currentPhase = MainPhase.GAME_RESULT
	
		break;
	case MainPhase.GAME_RESULT:
	
		if( !alarm[1] ){
			trace( "Setting alarm to clean up scores" );
			alarm[1] = room_speed * 30;	
			obj_lobby.currentPhase = LobbyPhase.INIT;
		}		
		// TODO: Add shader blur
	
		break;
	case MainPhase.DEMO:
		
		break;
}