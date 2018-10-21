/// @description Draw timer

if( currentValue > 0 ){
	draw_text( room_width/2, room_height/2, "" + string( currentValue/room_speed ) );
}