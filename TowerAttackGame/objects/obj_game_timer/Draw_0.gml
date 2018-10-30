/// @description Draw timer

if( currentValue > 0 ){
	draw_text_transformed( room_width/2, 100, string( round( currentValue/room_speed ) ), 5, 5, 0 );
}