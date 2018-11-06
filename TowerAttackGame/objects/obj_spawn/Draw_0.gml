/// @description Draw warning icon

draw_self();

if( showWarningIcon ){
		
	draw_sprite_ext( spr_place_your_token, 0, x,  y - 80, warningIconSize, warningIconSize, 0, c_white, 0.75 );

	// Shring and grow 10% every 2 seconds
	if( warningIconShrink ){
		warningIconSize = warningIconSize - 0.1 / room_speed * 2;
	} else {
		warningIconSize = warningIconSize + 0.1 / room_speed * 2; 
	}
	
	// Reverse shirnk/grow
	warningIconShrink = warningIconSize > 0.6 ? true : warningIconShrink;
	warningIconShrink = warningIconSize < 0.5 ? false : warningIconShrink;
	
}