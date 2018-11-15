/// @description Draw timer

draw_set_font( fnt_basic_font );
if( currentValue > 0 && obj_game.currentPhase == GamePhase.GAME ){
	draw_text( room_width/2, room_height*0.22, string( round( currentValue/room_speed ) ) );
}
draw_set_font( -1 );

	