/// @description Bomber explosion

///https://opengameart.org/content/2d-explosion-animations-frame-by-frame
sprite_index = unitExplodeAnimation;
image_index = 0;

var soldiersInRadius = scr_collision_circle_list( x, y, 300, obj_soldier, true, true );

if( soldiersInRadius != noone ){
	trace( "Exploding bomber, soldiers in radius=" + string( ds_list_size( soldiersInRadius ) ) );
}

if( soldiersInRadius != noone){
		
	var n = 0;
	while( n < ds_list_size( soldiersInRadius ) ){
		
		with( soldiersInRadius[| n] ){
			trace( "Burning soldier due to bomber explosion", self );
			if( soldierToFight != other ){
				// Don't burn the fighting soldier
				event_user( FightUnitEvent.BURN_BY_BOMBER );
			}
		}
		n += 1;
   }
   ds_list_destroy(soldiersInRadius);
}