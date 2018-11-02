/// @description Draw bridge stats

draw_set_halign( fa_center );

draw_text_transformed( x, y+100, "Fill: " + string( fillUp ), 2, 2, 0 );
draw_text_transformed( x, y+130, "Dura: " + string( durability ), 2, 2, 0 );
draw_text_transformed( x, y+160, "Shie: " + string( shield ), 2, 2, 0 );

if( durability > 0 ){
	targetBridgeBackground.visible = true;
} else {
	targetBridgeBackground.visible = false;
}