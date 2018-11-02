/// @description Spawner phases

if( previousPhase != currentPhase ){
	var previous = ds_list_find_value( phases, previousPhase );
	var current = ds_list_find_value( phases, currentPhase );
	trace( "Switch SpawnerPhase from " + previous + " ->  " + current );
	previousPhase = currentPhase;
}

switch( currentPhase ){
	case SpawnerPhase.INIT:
		
		if( obj_game.currentPhase == GamePhase.GAME ){
			currentPhase = SpawnerPhase.PREPARE_NEXT_WAVE;	
		}
	
		break;
	case SpawnerPhase.PREPARE_NEXT_WAVE:
	
		switch( nextWave ){
			case SpawnerWave.WAVE_01:
				alarm_set( nextWaveAlarmIndex, 1 * room_speed );
				
				// Useful for debugging, comment out the for loop below to have full 
				//ds_list_add( nextWaveTasks, scr_create_spawn_task( TeamId.RED, instance_find( obj_spawn_red, 1), SoldierType.BASIC ) );
				//ds_list_add( nextWaveTasks, scr_create_spawn_task( TeamId.RED, instance_find( obj_spawn_red, 1), SoldierType.BASIC ) );
				//ds_list_add( nextWaveTasks, scr_create_spawn_task( TeamId.RED, instance_find( obj_spawn_red, 1), SoldierType.BASIC ) );
				//ds_list_add( nextWaveTasks, scr_create_spawn_task( TeamId.RED, instance_find( obj_spawn_red, 1), SoldierType.BASIC ) );
	
				trace( "Preparing spawn tasks for wave", nextWave );
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					trace( "Preparing spawn tasks for team", teamId );
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 2; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						trace( "Preparing spawn task under specific spawn point", nrOfSpawns, spawn );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}		
			
				break;
			case SpawnerWave.WAVE_02:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				//ds_list_add( nextWaveTasks, scr_create_spawn_task( TeamId.RED, instance_find( obj_spawn_red, 1), SoldierType.BASIC ) );
				//ds_list_add( nextWaveTasks, scr_create_spawn_task( TeamId.BLUE, instance_find( obj_spawn_blue, 1), SoldierType.ELITE ) );
				
				trace( "Preparing spawn tasks for wave", nextWave );
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					trace( "Preparing spawn tasks for team", teamId );
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 3; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						trace( "Preparing spawn task under specific spawn point", nrOfSpawns, spawn );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_03:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 1; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_04:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 2; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_05:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 3; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BOMBER ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_06:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 2; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_07:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 3; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_08:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 4; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BOMBER ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BOMBER ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_09:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 1; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_10:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 2; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_11:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 3; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.ELITE ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_12:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 2; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BOMBER ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BOMBER ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BOMBER ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_13:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 3; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.ELITE ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.ELITE ) );
					}
				}
			
				break;
			case SpawnerWave.WAVE_14:
				alarm_set( nextWaveAlarmIndex, 10 * room_speed );
				
				// For both teams, create equal spawn tasks, values are from TeamId enum
				for( var teamId = 0; teamId < 2; teamId++ ){
					var randomSpawns = scr_choose_random_spawns( teamId );
					// Choose randomly X number of lanes and trigger soldiers on them
					for( var nrOfSpawns = 0; nrOfSpawns < 3; nrOfSpawns++ ){
						var spawn = ds_list_find_value( randomSpawns, nrOfSpawns );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.ELITE ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.BASIC ) );
						
						ds_list_add( nextWaveTasks, scr_create_spawn_task( teamId, spawn, SoldierType.ELITE ) );
					}
				}
				
				break;
			default:
				currentPhase = SpawnerPhase.WAVES_COMPLETE;
		}
		
		nextWave++; // Enum magic, switch to next wave
		currentPhase = SpawnerPhase.WAIT_FOR_WAVE;		
	
		break;
	case SpawnerPhase.WAIT_FOR_WAVE: // Triggered after preparing the wave
	
		// TODO: Trigger warning at some point
	
		break;
	case SpawnerPhase.TRIGGER_WAVE: // Triggered by alarm
	
		trace( "Triggering next wave", nextWave );
	
		for(var i = 0; i < ds_list_size( nextWaveTasks ); i++ ){
			var task = ds_list_find_value( nextWaveTasks, i );
			with( task.spawn ){
				ds_list_add( spawnTasks, task );
			}
		}
		
		ds_list_clear( nextWaveTasks );
		currentPhase = SpawnerPhase.PREPARE_NEXT_WAVE;
	
		break
	case SpawnerPhase.WAVES_COMPLETE:
	
		break;
}