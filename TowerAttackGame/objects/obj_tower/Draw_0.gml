/// @description Insert description here
// You can write your code in this editor

draw_self();
if( obj_game.currentPhase == GamePhase.GAME ){
	draw_set_halign( fa_center );
	draw_set_font( fnt_basic_font );
	draw_text_transformed( x, y - 230, string( towerHealth ), 0.5, 0.5, 0 );
	draw_set_font( -1 );
	
	var subImage = healthBarReverse ? 
						round( ( 20-towerHealth )/20 * sprite_get_number( healthBarSprite ) ) - 1 
					  : round( towerHealth/20 * sprite_get_number( healthBarSprite ) ) - 1;
	
	draw_sprite_ext( healthBarSprite, subImage, x, y - 150, 2, 2, 0, c_white, 1 );
}