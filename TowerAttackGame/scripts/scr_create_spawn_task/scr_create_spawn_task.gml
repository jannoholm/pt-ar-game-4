var targetTeam = argument0;
var targetSpawn = argument1;
var targetSoldierType = argument2;

var task = instance_create_layer( 0, 0, "lyr_gameplay", obj_spawn_task )
task.spawn = targetSpawn;
switch( targetTeam ){
	case TeamId.ONE:
		
		switch( targetSoldierType ){
			case SoldierType.BASIC:
				task.soldier = obj_soldier_lvl1_swordsman;
				break;
			case SoldierType.BOMBER:
				task.soldier = obj_soldier_lvl2_dwarven_sapper;
				break;
			case SoldierType.ELITE:
				task.soldier = obj_soldier_lvl3_horse_knight;
				break;
		}
		
		break;
	case TeamId.TWO:
		
		switch( targetSoldierType ){
			case SoldierType.BASIC:
				task.soldier = obj_soldier_lvl1_orc_warrior;
				break;
			case SoldierType.BOMBER:
				task.soldier = obj_soldier_lvl2_goblin_kamikaze;
				break;
			case SoldierType.ELITE:
				task.soldier = obj_soldier_lvl3_orc_wolf_raider;
				break;
		}
		
		break;
}

return task;