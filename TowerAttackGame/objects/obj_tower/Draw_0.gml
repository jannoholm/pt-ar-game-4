/// @description Insert description here
// You can write your code in this editor

draw_self();
if( obj_game.currentPhase == GamePhase.GAME ){
	draw_set_halign( fa_center );
	draw_text_transformed( x, y-170, string( towerHealth ), 3, 3, 0 );
}