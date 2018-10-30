var targetTeam = argument0;
var targetSpawn = argument1;
var targetSoldierType = argument2;

var task = instance_create_layer( 0, 0, "lyr_gameplay", obj_spawn_task )
task.spawn = targetSpawn;
switch( targetTeam ){
	case TeamId.RED:
		
		switch( targetSoldierType ){
			case SoldierType.BASIC:
				task.soldier = obj_soldier_red_swordsman;
				break;
			case SoldierType.BOMBER:
				task.soldier = obj_soldier_red_dwarven_sapper;
				break;
			case SoldierType.ELITE:
				task.soldier = obj_soldier_red_horse_knight;
				break;
		}
		
		break;
	case TeamId.BLUE:
		
		switch( targetSoldierType ){
			case SoldierType.BASIC:
				task.soldier = obj_soldier_blue_orc_warrior;
				break;
			case SoldierType.BOMBER:
				task.soldier = obj_soldier_blue_goblin_kamikaze;
				break;
			case SoldierType.ELITE:
				task.soldier = obj_soldier_blue_orc_wolf_raider;
				break;
		}
		
		break;
}

return task;