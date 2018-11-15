/// @description Init lobby

window_set_fullscreen(true);

enum MainPhase {
	INIT, // Start networking
	GO_TO_LOBBY, // Connected, go to lobby
	LOBBY, // Show lobby, choosing players
	GAME_INIT, // Create game elements
	GAME, // Game in progress
	GAME_END, // Clean-up game elements, send result
	GAME_RESULT, // Show leaderboard and init lobby
	WAIT_SERVER_SCORE, // Wait for server game result with new ELO ratings
	DEMO // Demo
}


phases = ds_list_create();
ds_list_add(phases, 
	"INIT", 
	"GO_TO_LOBBY", 
	"LOBBY", 
	"GAME_INIT", 
	"GAME", 
	"GAME_END", 
	"GAME_RESULT",
	"WAIT_SERVER_SCORE",
	"DEMO"
)

previousPhase = MainPhase.INIT;
currentPhase = MainPhase.INIT;
server = instance_create_layer( 0, 0, "lyr_tower_attack", obj_server_client );
server = instance_create_layer( 0, 0, "lyr_tower_attack", obj_sound_manager );

// #TODO: From conf
clientId = "TABLE";
email = "info@playtech.com";

if( obj_configuration.standAloneMode ){
	obj_tower_attack.currentPhase = MainPhase.GO_TO_LOBBY;
} else {
	scr_send_join_server(clientId, email);
}

lastGameScore = noone;
firstTimeStart = true;