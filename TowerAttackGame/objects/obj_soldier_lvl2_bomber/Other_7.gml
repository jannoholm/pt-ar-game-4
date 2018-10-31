/// @description Destroy after explosion

if( sprite_index == spr_soldier_lvl2_bomber_explode ){
	trace( "Animation end for bomber", self );
	instance_destroy();	
}