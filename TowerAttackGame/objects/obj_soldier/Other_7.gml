/// @description
if( sprite_index == unitFightAnimation ){
	trace( "Unit fight animation ended", self );
	sprite_index = unitDeadAnimation;
	image_index = 0;
} else if( sprite_index == unitDeadAnimation ){
	trace( "Unit dead animation ended", self );
	currentPhase = SoldierPhase.DEAD;
	// TODO: Add small fade away time with transparency
}
