/// @description Draw joined players

draw_set_halign( fa_center );

if( currentPhase == LobbyPhase.WAITING_PLAYERS ){

	draw_set_font( fnt_basic_font );
	if( playerOneTop == noone ){
		draw_text_transformed( room_width*0.20, 50, "< SHOW YOUR QR TO CAMERA", 0.85, 0.85, 0 );
	} else {
		draw_text_transformed( room_width*0.20, 50, "Current player: " + playerOneTop.playerName, 0.75, 0.75, 0 );
	}

	if( playerTwoTop == noone ){
		draw_text_transformed( room_width*0.80, 50, "SHOW YOUR QR TO CAMERA >", 0.85, 0.85, 0 );
	} else {
		draw_text_transformed( room_width*0.80, 50, "Current player: " + playerTwoTop.playerName, 0.75, 0.75, 0 );
	}

	draw_set_font( -1 );
}
