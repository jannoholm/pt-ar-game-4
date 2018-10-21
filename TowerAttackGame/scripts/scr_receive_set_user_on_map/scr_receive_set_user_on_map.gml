var buffer=argument[0];

var userId = buffer_read( buffer, buffer_s32 );
var userName = buffer_read( buffer, buffer_string );
var team = buffer_read( buffer, buffer_s8 );
var positionInTeam = buffer_read( buffer, buffer_s32 );

trace( "Received SetUserOnMapRequest: ", userId, userName, team, positionInTeam );

if( instance_exists( obj_lobby ) && obj_lobby.currentPhase == LobbyPhase.WAITING_PLAYERS ){
	var lobbyUser = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_user );
	lobbyUser.userId = userId;
	lobbyUser.userName = userName;

	if( team == Team.RED && positionInTeam == Position.TOP ){
		obj_lobby.userRedOne = lobbyUser;
	} else if( team == Team.RED && positionInTeam == Position.BOTTOM ){
		obj_lobby.userRedTwo = lobbyUser;
	} else if( team == Team.BLUE && positionInTeam == Position.TOP ){
		obj_lobby.userBlueOne = lobbyUser;
	} else if( team == Team.BLUE && positionInTeam == Position.BOTTOM ){
		obj_lobby.userBlueTwo = lobbyUser;
	} else {
		trace( "Unknown team/position combination" );	
	}
} else {
	trace("Received SetUserOnMapRequest at incorrect time" );	
}



