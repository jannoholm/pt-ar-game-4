/// @description Insert description here
// You can write your code in this editor

var battleAreaSoldiers = scr_instance_place_list( x, y, obj_soldier );

var soldierToFightTeamOne = noone;
var soldierToFightTeamTwo = noone;

if( battleAreaSoldiers != noone){
		
	var n = 0;
	while( n < ds_list_size( battleAreaSoldiers ) ){
		
		with( battleAreaSoldiers[| n] ){
			
			if( type != SoldierType.ELITE && team == obj_game.teamOne && currentPhase == SoldierPhase.FOLLOW_PATH ) {
				if( soldierToFightTeamOne == noone ) {
					soldierToFightTeamOne = self;	
				} else if( spawnIndex < soldierToFightTeamOne.spawnIndex ) {
					soldierToFightTeamOne = self;
				}
			}
			if( type != SoldierType.ELITE && team == obj_game.teamTwo && currentPhase == SoldierPhase.FOLLOW_PATH ) {
				if( soldierToFightTeamTwo == noone ) {
					soldierToFightTeamTwo = self;	
				} else if( spawnIndex < soldierToFightTeamTwo.spawnIndex ) {
					soldierToFightTeamTwo = self;
				}
			}
		}
		n += 1;
   }
   ds_list_destroy(battleAreaSoldiers);
}

if( soldierToFightTeamOne != noone && soldierToFightTeamTwo != noone ){
	show_debug_message("Found soldierrs to fight, one=" + string( soldierToFightTeamOne ) + ", two=" + string( soldierToFightTeamTwo ) );
	
	soldierToFightTeamOne.chargedSoldier = soldierToFightTeamTwo;
	soldierToFightTeamTwo.chargedSoldier = soldierToFightTeamOne;
	
	soldierToFightTeamOne.currentPhase = SoldierPhase.CHARGE_TO_FIGHT;	
	soldierToFightTeamTwo.currentPhase = SoldierPhase.CHARGE_TO_FIGHT;
}
