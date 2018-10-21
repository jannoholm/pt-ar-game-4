/// @description Dragging objects

if( mouse_check_button_pressed( mb_left ) ){
	with( obj_action_token ){
		if( !point_in_rectangle(mouse_x, mouse_y, x, y, x + sprite_width, y + sprite_height) ){
			continue;
		}
		
		//show_debug_message("Clicked: " + string(mouse_x) + " " + string(mouse_y) + "x" )

		if( other.dragObject == noone || other.dragObject.depth > depth ){
			other.dragObject = id;
			other.xRelative = x - mouse_x;
			other.yRelative = y - mouse_y;
		}
	}
	
	if( dragObject != noone ){
		scr_shift_depth( dragObject, obj_action_token );
	}
}

if( mouse_check_button_released( mb_left ) ){
	dragObject = noone;
	xRelative = 0;
	yRelative = 0;
}