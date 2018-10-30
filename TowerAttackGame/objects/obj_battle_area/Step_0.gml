/// @description Insert description here
// You can write your code in this editor

var battleAreaSoldiers = scr_instance_place_list( x, y, obj_soldier );

var soldierToFightRed = noone;
var soldierToFightBlue = noone;

if( battleAreaSoldiers != noone){
		
	var n = 0;
	while( n < ds_list_size( battleAreaSoldiers ) ){
		
		with( battleAreaSoldiers[| n] ){
			
			if( type != SoldierType.ELITE && team == obj_game.redTeam && currentPhase == SoldierPhase.FOLLOW_PATH ) {
				if( soldierToFightRed == noone ) {
					soldierToFightRed = self;	
				} else if( spawnIndex < soldierToFightRed.spawnIndex ) {
					soldierToFightRed = self;
				}
			}
			if( type != SoldierType.ELITE && team == obj_game.blueTeam && currentPhase == SoldierPhase.FOLLOW_PATH ) {
				if( soldierToFightBlue == noone ) {
					soldierToFightBlue = self;	
				} else if( spawnIndex < soldierToFightBlue.spawnIndex ) {
					soldierToFightBlue = self;
				}
			}
		}
		n += 1;
   }
   ds_list_destroy(battleAreaSoldiers);
}

if( soldierToFightRed != noone && soldierToFightBlue != noone ){
	show_debug_message("Found soldierrs to fight, red=" + string( soldierToFightRed ) + ", blue=" + string( soldierToFightBlue ) );
	
	soldierToFightRed.chargedSoldier = soldierToFightBlue;
	soldierToFightBlue.chargedSoldier = soldierToFightRed;
	
	soldierToFightRed.currentPhase = SoldierPhase.CHARGE_TO_FIGHT;	
	soldierToFightBlue.currentPhase = SoldierPhase.CHARGE_TO_FIGHT;
}
