/// @description Draw timer

draw_set_font( fnt_basic_font );
if( currentValue > room_speed * 2 ){
	draw_text_transformed( room_width/2, room_height*0.42, "READY", 1.8, 1.8, 0 );
} else if( currentValue > room_speed * 1 ){
	draw_text_transformed( room_width/2, room_height*0.42, "STEADY", 1.8, 1.8, 0 );
} else if( currentValue > 0 ){
	draw_text_transformed( room_width/2, room_height*0.42, "GO", 1.8, 1.8, 0 );
}
draw_set_font( -1 );