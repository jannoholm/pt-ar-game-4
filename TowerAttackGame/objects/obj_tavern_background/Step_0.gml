/// @description Active sprite

if( activeLastStep && !obj_tavern_permanent.activated ){
	image_index = 0;
	image_speed = 0;
	activeLastStep = false;
} else if ( !activeLastStep && obj_tavern_permanent.activated ){
	image_index = 0;
	image_speed = 1;
	activeLastStep = true;
}
