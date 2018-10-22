/// @description Draw joined players

if( playerRedOne == noone ){
	draw_text( 10, 50, "Scan QR code or press enter to  add defualt player" );
} else {
	draw_text( 10, 50, "Current player: " + playerRedOne.playerName );
}

if( playerRedTwo == noone ){
	draw_text( 10, room_height - 50, "Scan QR code or press enter to  add defualt player" );
} else {
	draw_text( 10, room_height - 50, "Current player: " + playerRedTwo.playerName );
}

if( playerBlueOne == noone ){
	draw_text( room_width - 100, 50, "Scan QR code or press enter to  add defualt player" );
} else {
	draw_text( room_width - 100, 50, "Current player: " + playerBlueOne.playerName );
}

if( playerBlueTwo == noone ){
	draw_text( room_width - 100, room_height - 50, "Scan QR code or press enter to  add defualt player" );
} else {
	draw_text( room_width - 100, room_height - 50, "Current player: " + playerBlueTwo.playerName );
}