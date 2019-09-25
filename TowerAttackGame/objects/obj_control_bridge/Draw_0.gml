/// @description Insert description here
// You can write your code in this editor

if( targetBridge.protected ) {
	image_index = 1; // Protected sub-image with padlock
} else if ( bridgeControlType == BridgeControlType.DESTROY && targetBridge.durability <= 0 ) {
	// Can't destroy a bridge that is already destroyed
	image_index = 1; // Protected sub-image with padlock
} else {
	image_index = 0; // Regular state
}

// #TODO Normalize values
image_blend = make_colour_rgb( 255 - chargeUp * 3, 255 - chargeUp * 3, 255 - chargeUp * 3 );
image_alpha = 0.75;

draw_self();