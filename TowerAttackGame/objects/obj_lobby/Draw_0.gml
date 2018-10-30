/// @description Draw joined players

draw_set_halign( fa_center );

if( playerRedOne == noone || playerBlueOne == noone ){
	draw_text_transformed( room_width/2, room_height/3, "SCAN QR CODE TO ADD PLAYER", 4, 4, 0 );
} else {
	draw_text_transformed( room_width/2, room_height/3, "ALL PLAYERS SET UP", 4, 4, 0 );
}

if( playerRedOne == noone ){
	draw_text_transformed( room_width*0.15, 50, "Current player: not set", 3, 3, 0 );
} else {
	draw_text_transformed( room_width*0.15, 50, "Current player: " + playerRedOne.playerName, 3, 3, 0 );
}

if( playerBlueOne == noone ){
	draw_text_transformed( room_width*0.85, 50, "Current player: not set", 3, 3, 0 );
} else {
	draw_text_transformed( room_width*0.85, 50, "Current player: " + playerBlueOne.playerName, 3, 3, 0 );
}