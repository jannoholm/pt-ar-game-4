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
