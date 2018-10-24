/// @description Draw last game result

if( currentPhase != MainPhase.GAME && lastGameScore != noone) {
	draw_text( room_width/2, room_height/2, "we have some score" );
} else if( currentPhase != MainPhase.GAME ){
	draw_text( room_width/2, room_height/2, "no score yet" );
}
