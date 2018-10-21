/// @description Validate dragging

if( mouse_check_button( mb_left ) ){
	if( dragObject != noone ){
		dragObject.x = mouse_x + xRelative;
		dragObject.y = mouse_y + yRelative;
	}
}