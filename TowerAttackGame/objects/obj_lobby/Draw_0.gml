/// @description Draw joined players

draw_set_halign( fa_center );

if( currentPhase == LobbyPhase.WAITING_PLAYERS ){

	draw_set_font( fnt_game_result );
	if( playerOneTop == noone ){
		draw_text_transformed( room_width*0.15, 50, "SCAN QR CODE TO ADD PLAYER", 0.5, 0.5, 0 );
	} else {
		draw_text_transformed( room_width*0.15, 50, "Current player: " + playerOneTop.playerName, 0.5, 0.5, 0 );
	}

	if( playerTwoTop == noone ){
		draw_text_transformed( room_width*0.85, 50, "SCAN QR CODE TO ADD PLAYER", 0.5, 0.5, 0 );
	} else {
		draw_text_transformed( room_width*0.85, 50, "Current player: " + playerTwoTop.playerName, 0.5, 0.5, 0 );
	}
}
