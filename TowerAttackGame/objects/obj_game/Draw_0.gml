/// @description Draw guidelines

draw_set_font( fnt_game_result );
if( currentPhase == GamePhase.WAIT_PLAYERS_READY ){
	draw_text( room_width/2, room_height*0.22, string( "PLACE YOUR TOKENS" ) );
		
	if( !obj_control_init_game_team_one.activated ){
		draw_sprite_ext( spr_place_your_token, 0, obj_control_init_game_team_one.x,  obj_control_init_game_team_one.y - 120, instructionIconSize, instructionIconSize, 0, c_white, 0.75 );
	}
	if( !obj_control_init_game_team_two.activated ){
		draw_sprite_ext( spr_place_your_token, 0, obj_control_init_game_team_two.x,  obj_control_init_game_team_two.y - 120, instructionIconSize, instructionIconSize, 0, c_white, 0.75 );
	}
	
	// Shring and grow 10% every 2 seconds
	if( instructionIconShrink ){
		instructionIconSize = instructionIconSize - 0.1 / room_speed * 2;
	} else {
		instructionIconSize = instructionIconSize + 0.1 / room_speed * 2; 
	}
	
	// Reverse shirnk/grow
	instructionIconShrink = instructionIconSize > 1 ? true : instructionIconShrink;
	instructionIconShrink = instructionIconSize < 0.9 ? false : instructionIconShrink;
	
}
draw_set_font( -1 );