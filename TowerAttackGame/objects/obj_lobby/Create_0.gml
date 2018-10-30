/// @description Init lobby

enum LobbyPhase {
	INIT, // Create player objects and start listening for players
	WAITING_PLAYERS, // Wait for player add events
	PLAYERS_FULL // Move the main phase further
}


phases = ds_list_create();
ds_list_add(phases, 
	"INIT", 
	"WAITING_PLAYERS", 
	"PLAYERS_FULL"
)

previousPhase = LobbyPhase.INIT;
currentPhase = LobbyPhase.INIT;


playerRedOne = noone;
playerRedTwo = noone;

playerBlueOne = noone;
playerBlueTwo = noone;

// Initially this game is 1v1
if( playerRedTwo == noone ){
	playerRedTwo = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerRedTwo.playerId = 0;
	playerRedTwo.playerName = "TABLE";
}
if( playerBlueTwo == noone ){
	playerBlueTwo = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerBlueTwo.playerId = 0;
	playerBlueTwo.playerName = "TABLE";
}
