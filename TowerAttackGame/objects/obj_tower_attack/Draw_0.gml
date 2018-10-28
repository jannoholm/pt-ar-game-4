/// @description Draw last game result

draw_set_halign( fa_center );

if( currentPhase == MainPhase.INIT ){
	draw_text_transformed( room_width/2, room_height/2, "CONNECTING...", 4, 4, 0 );
}