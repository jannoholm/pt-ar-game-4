/// @description Fix bridges in demo mode

if( obj_game.currentPhase == GamePhase.DEMO ){
	trace( "FIXING ALL BRIDGES" );
	with( obj_bridge ){
		durability = 100;
	}
}