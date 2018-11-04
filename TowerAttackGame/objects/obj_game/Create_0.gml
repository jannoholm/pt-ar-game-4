/// @description Init lobby

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
with( instance_create_layer( 370, 930, "lyr_elements", obj_spawn_team_one ) ) {
	path = path_middle_high;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3500, 927, "lyr_elements", obj_spawn_team_two ) ) {
	path = path_middle_high;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}
with( instance_create_layer( 420, 1190, "lyr_elements", obj_spawn_team_one ) ) {
	path = path_middle_low;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3440, 1180, "lyr_elements", obj_spawn_team_two ) ) {
	path = path_middle_low;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}
with( instance_create_layer( 55, 1150, "lyr_elements", obj_spawn_team_one ) ) {
	path = path_bottom_from_left;
	pathDirection = 1;
	pathStartPosition = 0.05;
	team = other.teamOne;
}
with( instance_create_layer( 3665, 1140, "lyr_elements", obj_spawn_team_two ) ) {
	path = path_bottom_from_right;
	pathDirection = -1;
	pathStartPosition = 0.95;
	team = other.teamTwo;
}

// Controls drag-n-drop for the action figures
instance_create_layer( -10, -10, "lyr_action_tokens", obj_drag_controller );
