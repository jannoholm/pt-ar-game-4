/// @description Insert description here
// You can write your code in this editor

var battleAreaSoldiersRed = scr_instance_place_list( x, y, obj_soldier_red );

with( battleAreaSoldiersRed ){
}


var soldierToFightRed = noone;

if( battleAreaSoldiersRed != noone){
	var n = 0;
	while( n < ds_list_size( battleAreaSoldiersRed ) ){
		
		with( battleAreaSoldiersRed[| n] ){
			
			if( currentState == STATE_SOLDIER.FOLLOW_PATH ) {
				if( soldierToFightRed == noone ) {
					soldierToFightRed = self;	
				} else if( spawnIndex < soldierToFightRed.spawnIndex ) {
					soldierToFightRed = self;
				}	
			}
		}
		n += 1;
   }
   ds_list_destroy(battleAreaSoldiersRed);
}

var battleAreaSoldiersBlue = scr_instance_place_list( x, y, obj_soldier_blue );

var soldierToFightBlue = noone;

if( battleAreaSoldiersBlue != noone){
	var n = 0;
	while( n < ds_list_size( battleAreaSoldiersBlue ) ){
		with( battleAreaSoldiersBlue[| n] ){
			
			if( currentState == STATE_SOLDIER.FOLLOW_PATH ) {
				if( soldierToFightBlue == noone ) {
					soldierToFightBlue = self;	
				} else if( spawnIndex < soldierToFightBlue.spawnIndex ) {
					soldierToFightBlue = self;
				}	
			}
		}
		n += 1;
   }
   ds_list_destroy(battleAreaSoldiersBlue);
}

if( soldierToFightRed != noone && soldierToFightBlue != noone ){
	show_debug_message("Found soldierrs to fight, red=" + string( soldierToFightRed ) + ", blue=" + string( soldierToFightBlue ) );
	
	soldierToFightRed.chargedSoldier = soldierToFightBlue;
	soldierToFightBlue.chargedSoldier = soldierToFightRed;
	
	soldierToFightRed.currentState = STATE_SOLDIER.CHARGE_TO_FIGHT;	
	soldierToFightBlue.currentState = STATE_SOLDIER.CHARGE_TO_FIGHT;
}
