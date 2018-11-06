/// @description Draw timer

draw_set_font( fnt_game_result );
if( currentValue > room_speed * 2 ){
	draw_text( room_width/2, room_height*0.22, "READY" );
} else if( currentValue > room_speed * 1 ){
	draw_text( room_width/2, room_height*0.22, "STEADY" );
} else if( currentValue > 0 ){
	draw_text( room_width/2, room_height*0.22, "GO" );
}
draw_set_font( -1 );