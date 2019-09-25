/// @description Degrade bridge

if( targetBridge.durability >= 50 ){
	visible = true;
	image_index = 0;
	image_alpha = 1;
} else if( targetBridge.durability > 0 ){
	visible = true;
	image_index = 1;
	image_alpha = 1;
} else if( targetControlArea.chargeUp > 0 ) {
	visible = true;
	image_index = 0;
	image_alpha = 0.3;
} else {
	visible = false;
}