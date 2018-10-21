/// @description Init lobby

enum GamePhase {
	INIT, // Create game elements
	COUNTDOWN, // Countdown timer
	GAME_START, // Countdown timer full
	GAME, // Game in progress
	GAME_END // Game has ended
}


phases = ds_list_create();
ds_list_add(phases, 
	"INIT",
	"COUNTDOWN",
	"GAME_START",
	"GAME",
	"GAME_END"
)

previousPhase = LobbyPhase.INIT;
currentPhase = LobbyPhase.INIT;


redPlayer = instance_create_layer(20, 20, "lyr_gameplay", obj_player_red);
bluePlayer = instance_create_layer(920, 20, "lyr_gameplay", obj_player_blue);

redTower = instance_create_layer(60, 320, "lyr_elements", obj_tower_red);
blueTower = instance_create_layer(980, 320, "lyr_elements", obj_tower_blue);

//obj_soldier_red_top
//obj_soldier_blue_top
//obj_soldier_red_middle_high
//obj_soldier_blue_middle_high
//obj_soldier_blue_middle_low
//obj_soldier_red_middle_low
//obj_soldier_red_bottom
//obj_soldier_blue_bottom
with( instance_create_layer(100, 170, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_red_top;
	player = other.redPlayer;
}
with( instance_create_layer(930, 170, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_blue_top;
	player = other.bluePlayer;
}
with( instance_create_layer(170, 255, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_red_middle_high;
	player = other.redPlayer;
}
with( instance_create_layer(880, 260, "lyr_elements", obj_spawn)) {
	soldierToSpawn = obj_soldier_blue_middle_high;
	player = other.bluePlayer;
}
with( instance_create_layer(175, 380, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_red_middle_low;
	player = other.redPlayer;
}
with( instance_create_layer(870, 365, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_blue_middle_low;
	player = other.bluePlayer;
}
with( instance_create_layer(95, 440, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_red_bottom;
	player = other.redPlayer;
}
with( instance_create_layer(920, 420, "lyr_elements", obj_spawn) ) {
	soldierToSpawn = obj_soldier_blue_bottom;
	player = other.bluePlayer;
}


// Controls drag-n-drop for the action figures
instance_create_layer(-10, -10, "lyr_action_tokens", obj_drag_controller);
