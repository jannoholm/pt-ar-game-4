/// @description Bomber explosion

///https://opengameart.org/content/2d-explosion-animations-frame-by-frame
sprite_index = spr_soldier_lvl2_bomber_explode;

var soldiersInRadius = scr_collision_circle_list( x, y, 300, obj_soldier, true, true );

if( soldiersInRadius != noone ){
	trace( "Exploding bomber, soldiers in radius=" + string( ds_list_size( soldiersInRadius ) ) );
}

with( soldiersInRadius ){
}

if( soldiersInRadius != noone){
		
	var n = 0;
	while( n < ds_list_size( soldiersInRadius ) ){
		
		with( soldiersInRadius[| n] ){
			trace( "Burning soldier due to bomber explosion", self );
			event_user( 3 );
		}
		n += 1;
   }
   ds_list_destroy(soldiersInRadius);
}