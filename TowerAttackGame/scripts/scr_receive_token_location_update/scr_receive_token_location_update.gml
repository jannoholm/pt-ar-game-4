// TokenLocationUpdateMessage
var buffer=argument[0];

var tokenId = buffer_read( buffer, buffer_string );
var team = buffer_read( buffer, buffer_s8 );
var tokenType = buffer_read( buffer, buffer_s8 );
var tokenIndex = buffer_read( buffer, buffer_s8 );
var locationX = buffer_read( buffer, buffer_s32 );
var locationY = buffer_read( buffer, buffer_s32 );

trace( "Received TokenLocationUpdateMessage", tokenId, team, tokenType, tokenIndex, locationX, locationY );

var token = noone;

if( instance_exists( obj_game ) && obj_game.currentPhase == GamePhase.GAME ){

	if( team == TeamId.ONE && tokenType == TokenType.BRIDGE ){
		var token = instance_find( obj_action_bridge_team_one, tokenIndex );
	} else if( team == TeamId.TWO && tokenType == TokenType.BRIDGE ){
		var token = instance_find( obj_action_bridge_team_two, tokenIndex );
	} else {
		trace( "Unknown team/token combination" );	
	}
	
} else {
	trace("Received TokenLocationUpdateMessage at incorrect time" );	
}

if( token != noone ){
	with( token ){
		x = locationX; // * room_width / 150; // #TODO: Define max constant
		y = locationY; // * room_height / 50; // #TODO: Define max constant
	}
}
