/// @description Init draggable objects

dragObject = noone;
xRelative = 0;
yRelative = 0;

//with( instance_create_layer( room_width*0.15, 100, "lyr_action_tokens", obj_action_bridge_team_one ) ) {
//	team = obj_game.teamOne;
//}
with( instance_create_layer( room_width*0.20, 100, "lyr_action_tokens", obj_action_bridge_team_one ) ) {
	team = obj_game.teamOne;
}
//with( instance_create_layer( room_width*0.80, 100, "lyr_action_tokens", obj_action_bridge_team_two ) ) {
//	team = obj_game.teamTwo;
//}
with( instance_create_layer( room_width*0.85, 100, "lyr_action_tokens", obj_action_bridge_team_two ) ) {
	team = obj_game.teamTwo;
}

//// Spawn figures team one
//with( instance_create_layer(40, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.teamOne;
//}
//with( instance_create_layer(70, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.teamOne;
//}
//with( instance_create_layer(100, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.teamOne;
//}

//// Spawn figures team two
//with( instance_create_layer(880, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.teamTwo;
//}
//with( instance_create_layer(910, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.teamTwo;
//}
//with( instance_create_layer(940, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.teamTwo;
//}



// Create a sort depth to start with
startDepth = 0;
with( obj_action_token) {
	depth = other.startDepth;
	other.startDepth++;
}