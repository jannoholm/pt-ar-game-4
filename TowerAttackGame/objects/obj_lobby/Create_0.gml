/// @description Init lobby

enum LobbyPhase {
	INIT, // Create player objects and start listening for players
	WAITING_PLAYERS, // Wait for player add events
	PLAYERS_FULL, // Move the main phase further
	COMPLETE // Player registration process done
}


phases = ds_list_create();
ds_list_add(phases, 
	"INIT", 
	"WAITING_PLAYERS", 
	"PLAYERS_FULL",
	"COMPLETE"
)

previousPhase = LobbyPhase.INIT;
currentPhase = LobbyPhase.INIT;


playerOneTop = noone;
playerOneBottom = noone;

playerTwoTop = noone;
playerTwoBottom = noone;

// Initially this game is 1v1
if( playerOneBottom == noone ){
	playerOneBottom = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerOneBottom.playerId = 0;
	playerOneBottom.playerName = "TABLE";
}
if( playerTwoBottom == noone ){
	playerTwoBottom = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerTwoBottom.playerId = 0;
	playerTwoBottom.playerName = "TABLE";
}

// Start game in demo mode
room_goto_next();