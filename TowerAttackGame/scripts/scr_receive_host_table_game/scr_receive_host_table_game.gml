// HostTableGameResponse
var buffer=argument[0];

var gameId = buffer_read( buffer, buffer_string );

trace( "Received HostTableGameResponse", gameId );
obj_server_client.gameId = gameId;
obj_tower_attack.currentPhase = MainPhase.GO_TO_LOBBY;

