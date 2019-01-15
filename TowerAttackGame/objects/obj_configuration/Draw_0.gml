/// @description Insert description here
// You can write your code in this editor

draw_set_font( fnt_basic_font );

if( global.debug ){
	draw_set_halign(fa_left);
	draw_text_transformed( room_width*0.9, room_height*0.08, 
	"D: turn debug text on/off\n" +
	"Enter: Add player during players waiting phase or init game when waiting for token placement\n" +
	"Delete: Remove a player during player waiting phase (only if 1 player has joined)\n" +
	"UP/DOWN: Background music volume up/down (does not affect sound effects)\n" +
	"SPACE: show middle of the map\n"
	, 0.3, 0.3, 180 );
}

draw_set_font( -1 );