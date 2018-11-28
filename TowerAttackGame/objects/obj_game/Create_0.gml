/// @description Init game

enum GamePhase {
	INIT, // Create game elements
	WAIT_PLAYERS_READY, // Wait for token placement
	COUNTDOWN, // Countdown timer
	GAME_START, // Countdown timer full
	GAME, // Game in progress
	GAME_END, // Game has ended
	DEMO // Keep spawning soldiers
}


phases = ds_list_create();
ds_list_add(phases, 
	"INIT",
	"WAIT_PLAYERS_READY",
	"COUNTDOWN",
	"GAME_START",
	"GAME",
	"GAME_END",
	"DEMO"
)

previousPhase = LobbyPhase.INIT;
currentPhase = LobbyPhase.INIT;


teamOne = instance_create_layer( room_width*0.15, 20, "lyr_gameplay", obj_team_one );
teamTwo = instance_create_layer( room_width*0.85, 20, "lyr_gameplay", obj_team_two );

teamOneTower = instance_create_layer( 130, 950, "lyr_elements", obj_tower_team_one );
teamTwoTower = instance_create_layer( 3720, 950, "lyr_elements", obj_tower_team_two );

instructionIconShrink = true;
instructionIconSize = 1; // Add a little wobble effect

// Top
with( instance_create_layer( 60, 670, "lyr_elements", obj_spawn_team_one) ) {
	path = path_top;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3770, 645, "lyr_elements", obj_spawn_team_two ) ) {
	path = path_top;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}
// Middle
with( instance_create_layer( 370, 930, "lyr_elements", obj_spawn_team_one ) ) {
	path = path_middle_spawn_soldier;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3500, 927, "lyr_elements", obj_spawn_team_two ) ) {
	path = path_middle_spawn_orc;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}
// Bottom inner
with( instance_create_layer( 215, 1220, "lyr_elements", obj_spawn_team_one ) ) {
	path = path_bottom_inner;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3660, 1200, "lyr_elements", obj_spawn_team_two ) ) {
	path = path_bottom_inner;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}
// Bottom outer
with( instance_create_layer( 60, 1200, "lyr_elements", obj_spawn_team_one ) ) {
	path = path_bottom_outer;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3808, 1155, "lyr_elements", obj_spawn_team_two ) ) {
	// Note: cooridnates are swapped on right side since the paths cross in the middle
	path = path_bottom_outer;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}

// Controls drag-n-drop for the action figures
instance_create_layer( -10, -10, "lyr_action_tokens", obj_drag_controller );
