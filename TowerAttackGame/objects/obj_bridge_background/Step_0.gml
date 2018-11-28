/// @description Degrade bridge

if( targetBridge.durability >= 50 ){
	visible = true;
	image_index = 0;
} else if( targetBridge.durability > 0 ){
	visible = true;
	image_index = 1;
} else {
	visible = false;	
}