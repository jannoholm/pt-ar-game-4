/// @description Draw joined users

if( userRedOne == noone ){
	draw_text( 10, 50, "Scan QR code or press enter to  add defualt user" );
} else {
	draw_text( 10, 50, "Current user: " + userRedOne.userName );
}

if( userRedTwo == noone ){
	draw_text( 10, room_height - 50, "Scan QR code or press enter to  add defualt user" );
} else {
	draw_text( 10, room_height - 50, "Current user: " + userRedTwo.userName );
}

if( userBlueOne == noone ){
	draw_text( room_width - 100, 50, "Scan QR code or press enter to  add defualt user" );
} else {
	draw_text( room_width - 100, 50, "Current user: " + userBlueOne.userName );
}

if( userBlueTwo == noone ){
	draw_text( room_width - 100, room_height - 50, "Scan QR code or press enter to  add defualt user" );
} else {
	draw_text( room_width - 100, room_height - 50, "Current user: " + userBlueTwo.userName );
}