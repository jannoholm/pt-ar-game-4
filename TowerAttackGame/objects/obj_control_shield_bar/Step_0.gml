/// @description Update shield bar

if( !shieldActive && targetProtectedElement.protected ){
	trace( "Switching shield on" );
	image_speed = 1;
	image_index = 0;
	shieldActive = true;
}