/// @description Active sprite

if( activeLastStep && !obj_tavern_permanent.activated ){
	sprite_index = spr_tavern_background;
	image_index = 0;
	activeLastStep = false;
} else if ( !activeLastStep && obj_tavern_permanent.activated ){
	sprite_index = spr_tavern_background_active;
	image_index = 0;
	activeLastStep = true;
}
