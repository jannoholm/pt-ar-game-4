var targetTeamId = argument0;

var spawns = ds_list_create();
if( targetTeamId == TeamId.ONE ){
	with( obj_spawn_team_one ){
		trace( "Adding team one spawn", id );
		ds_list_add( spawns, id );	
	}
} 
if( targetTeamId == TeamId.TWO ){
	with( obj_spawn_team_two ){
		trace( "Adding team two spawn", id );
		ds_list_add( spawns, id );	
	}
}

trace( "Before shuffle", ds_list_find_value( spawns, 0 ), ds_list_find_value( spawns, 1 ), ds_list_find_value( spawns, 2 ), ds_list_find_value( spawns, 3 ) );

ds_list_shuffle( spawns );

trace( "After shuffle", ds_list_find_value( spawns, 0 ), ds_list_find_value( spawns, 1 ), ds_list_find_value( spawns, 2 ), ds_list_find_value( spawns, 3 ) );

return spawns;