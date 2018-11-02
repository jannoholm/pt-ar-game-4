/// @description Fight unit basic

trace( "Elite unit fighting basic unit, current HP=" + string( hp ) );

if( --hp == 0 ){
	trace( "Elite unite 0 HP left, executing parent logic" )
	event_inherited();
	soldierToFight.sprite_index = soldierToFight.unitFightAnimation;
	soldierToFight.image_index = 0;
	soldierToFight.depth = depth + 1;
} else {

	soldierToFight.sprite_index = soldierToFight.unitDeadAnimation;
	soldierToFight.image_index = 0;
	soldierToFight.depth = depth + 1;
	soldierToFight.image_alpha = 0.5;
	
	sprite_index = unitSlashAnimation;
	image_index = 0;
}