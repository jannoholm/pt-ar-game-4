/// @description Insert description here
// You can write your code in this editor
draw_self();

// #TODO Normalize values
image_blend = make_colour_rgb( 255 - chargeUp * 3, 255 - chargeUp * 3, 255 - chargeUp * 3 );
image_alpha = 0.75;

if( targetTavern.protected ){
	image_index = 1; // Protected sub-image with padlock
} else {
	image_index = 0; // Regular state
}