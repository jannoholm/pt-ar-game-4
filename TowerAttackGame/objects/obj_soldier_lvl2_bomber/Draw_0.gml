/// @description Draw sprite

draw_circle( x, y, 300, true );

draw_self();

if( sprite_index == spr_soldier_lvl2_bomber_explode ){
	image_blend = c_white;
} else {
	image_blend = make_colour_rgb( 170, 0, 0 );
}
