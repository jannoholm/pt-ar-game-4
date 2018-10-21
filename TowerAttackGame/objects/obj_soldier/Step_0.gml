/// @description Insert description here
// You can write your code in this editor

// GameMaker sprite orientation is assumed to be towrds left, but our sprites are towards top
image_angle = direction - 90;

switch( currentState ){
	case STATE_SOLDIER.SPAWN:
		
		path_start(path, pathDirection * pathMoveSpeed, path_action_stop, true);
		path_position = pathStartPosition;
		currentState = STATE_SOLDIER.FOLLOW_PATH;
		
		break;
	case STATE_SOLDIER.FOLLOW_PATH:
	
		var currentPath = path_index;
		var soldiersInFront = noone;

		for ( var i = 0; i < instance_number(obj_soldier); i++ ) {
			var tempSoldier = instance_find(obj_soldier, i);
			// #TODO Tavern check and age check after tavern
			if ( tempSoldier.path_index == path_index && tempSoldier.pathDirection == pathDirection && tempSoldier.spawnIndex < spawnIndex ) {
				// Non-enemy soldier on same path in front
				soldiersInFront[i] = tempSoldier;
			}
		}

		var pauseMovement = false;

		for ( var i = 0; i < array_length_1d(soldiersInFront); i++ ) {
	
			if ( instance_exists(soldiersInFront[i]) ) {
				// Another soldier on path is in front, measure distance
				//var distanceToFrontSoldier = distance_to_object( soldiersInFront[i] );
				var distanceToFrontSoldier = abs( soldiersInFront[i].path_position - path_position );
		
				if ( distanceToFrontSoldier < 0.015 ) {
					// Distance to next soldier is  too small
					pauseMovement = true;
					break;		
				}
			}
		}

		if ( pauseMovement ) {
			path_speed = 0;	
		} else {
			path_speed = pathDirection * pathMoveSpeed;	
		}
	
		break;
	case STATE_SOLDIER.ENTER_TUNNEL:
			
		// Nothing different, just hidden
		visible = false;
		currentState = STATE_SOLDIER.IN_TUNNEL;
		
		break;
	case STATE_SOLDIER.IN_TUNNEL:
			
		// Nothing different...
			
		break;
	case STATE_SOLDIER.EXIT_TUNNEL:

		// Unhide and continue path
		visible = true;
		currentState = STATE_SOLDIER.FOLLOW_PATH;
			
		break;
	case STATE_SOLDIER.FIGHT:
			
		// If alarm has not been set yet, destory the soldier with a delay
		if( alarm_get(unitDeadAlarmIndx) == -1 ) {
			path_end();
			speed = 0;
			sprite_index = fightingSprite;
			alarm_set( unitDeadAlarmIndx, room_speed );	
		}
	
		break;
	case STATE_SOLDIER.CHARGE_TO_FIGHT:
			
		path_end();
		move_towards_point(chargedSoldier.x, chargedSoldier.y, pathMoveSpeed * 1.5);
	
		break;
	case STATE_SOLDIER.GO_TO_TAVERN:
	
		visible = false;
	
		path_end();
		// move_towards_point( x, y, pathMoveSpeed );
	
		break;
	case STATE_SOLDIER.IN_TAVERN:
		
		visible = false;
		
		break;
	case STATE_SOLDIER.SPAWN_FROM_TAVERN:
	
		spawnIndex = global.soldierSpawnIndex++; // Consider it a fresh spawn for ordering of soldiers
		path_start(path, pathDirection * pathMoveSpeed, path_action_stop, true);
		path_position = 0.5; // #TODO Variable set by child
		visible = true;
		currentState = STATE_SOLDIER.FOLLOW_PATH;
	
		break;
}
