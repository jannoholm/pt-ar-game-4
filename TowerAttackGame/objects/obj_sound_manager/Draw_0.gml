/// @description Insert description here
// You can write your code in this editor


if( showSoundLevelInfo ){
	draw_set_font( fnt_game_result );
	draw_text( room_width/2, room_height/2, string( round( currentBackgroundLevel*100 ) ) + "%" );
	draw_set_font( -1 );	
}