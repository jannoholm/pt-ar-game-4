/// @description Soldier phases

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch SoldierPhase from " + previous + " -> " + current, self );
	previousPhase = currentPhase;
}

switch( currentPhase ){
	case SoldierPhase.SPAWN:
		
		path_start(path, pathDirection * pathMoveSpeed, path_action_stop, true);
		path_position = pathStartPosition;
		currentPhase = SoldierPhase.FOLLOW_PATH;
		
		break;
	case SoldierPhase.FOLLOW_PATH:
	
		if( soldierInFront == noone || !instance_exists( soldierInFront ) || soldierInFront.currentPhase != SoldierPhase.FOLLOW_PATH ){
			
			soldierInFront = noone; // Reset in case there is no soldier in front
			var nrOfSoldiers = instance_number(obj_soldier);
			var minimumDistance = 1;			

			for ( var i = 0; i < nrOfSoldiers; i++ ) {
				var tempSoldier = instance_find( obj_soldier, i );
				if( tempSoldier.currentPhase == SoldierPhase.FOLLOW_PATH 
					&& tempSoldier.type == type 
					&& tempSoldier.path_index == path_index 
					&& tempSoldier.pathDirection == pathDirection 
					&& ( tempSoldier.path_position - path_position ) * pathDirection > 0 ) {
					// Non-enemy soldier on same path in front
						
					var distanceToFrontSoldier = abs( tempSoldier.path_position - path_position );
					if( distanceToFrontSoldier < minimumDistance ){
						// Update best candidate to minimum distance
						soldierInFront = tempSoldier;
						minimumDistance = distanceToFrontSoldier;
					}
				}
			}
		}
	
		if( soldierInFront != noone && instance_exists( soldierInFront ) ){
			
			var distanceToFrontSoldier = abs( soldierInFront.path_position - path_position );	
			
			// y = ( x - 0.01 ) * 100
			// speedLimiter = ( distance - 0.01 ) * 100
			// 0.01 is 0 and 0.02 is 1
			// So if unit is closer than 0.01, then stop the unit
			// If unit is further than 0.02, then move at max speed
			path_speed = pathDirection * pathMoveSpeed * clamp( ( distanceToFrontSoldier - 0.01 ) * 100 , 0, 1 );
		} else {
			path_speed = pathDirection * pathMoveSpeed;
		}
	
		break;
	case SoldierPhase.ENTER_TUNNEL:
			
		// Nothing different, just hidden
		visible = false;
		currentPhase = SoldierPhase.IN_TUNNEL;
		
		break;
	case SoldierPhase.IN_TUNNEL:
			
		// Nothing different...
			
		break;
	case SoldierPhase.EXIT_TUNNEL:

		// Unhide and continue path
		visible = true;
		currentPhase = SoldierPhase.FOLLOW_PATH;
			
		break;
	case SoldierPhase.INIT_FIGHT:
	
		trace( "Starting fight", self, soldierToFight );
		
		path_end();
		speed = 0;		
		
		if( soldierToFight.type == SoldierType.BASIC ) {
			event_user( FightUnitEvent.BASIC );
		} else if( soldierToFight.type == SoldierType.BOMBER ) {
			event_user( FightUnitEvent.BOMBER );
		} else if( soldierToFight.type == SoldierType.ELITE ) {
			event_user( FightUnitEvent.ELITE );
		} else {
			trace( "Failed to detect soldier type to fight", soldierToFight );	
		}
		
		if( currentPhase == SoldierPhase.INIT_FIGHT ){
			currentPhase = SoldierPhase.FIGHT;
		} else {
			trace( "Soldier phase changed by some other logic", self );
		}
	
		break;
	case SoldierPhase.FIGHT:
		
		break;
	case SoldierPhase.CHARGE_TO_FIGHT:
			
		path_end();
		if( instance_exists( chargedSoldier ) ) {
			move_towards_point(chargedSoldier.x, chargedSoldier.y, pathMoveSpeed * 2);
		} else {
			// #TODO: Quick fix for exploded/charged soldier, actually the soldier should find next soldier to charge or continue path
			event_user( FightUnitEvent.ELITE );
		}
	
		break;
	case SoldierPhase.GO_TO_TAVERN:
	
		// #TODO: Move to point X
		instance_destroy();
	
		break;
	case SoldierPhase.IN_TAVERN:
		
		visible = false;
		
		break;
	case SoldierPhase.SPAWN_FROM_TAVERN:
	
		spawnIndex = global.soldierSpawnIndex++; // Consider it a fresh spawn for ordering of soldiers
		path_start(path, pathDirection * pathMoveSpeed, path_action_stop, true);
		path_position = 0.5; // #TODO Variable set by child
		visible = true;
		currentPhase = SoldierPhase.FOLLOW_PATH;
	
		break;
	case SoldierPhase.DEAD:
		instance_destroy();
	
		break;
}
