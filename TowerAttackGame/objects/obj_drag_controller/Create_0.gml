/// @description Init draggable objects

dragObject = noone;
xRelative = 0;
yRelative = 0;

with( instance_create_layer(40, 40, "lyr_action_tokens", obj_action_bridge_red) ) {
	team = obj_game.redTeam;
}
with( instance_create_layer(70, 40, "lyr_action_tokens", obj_action_bridge_red) ) {
	team = obj_game.redTeam;
}
with( instance_create_layer(910, 40, "lyr_action_tokens", obj_action_bridge_blue) ) {
	team = obj_game.blueTeam;
}
with( instance_create_layer(940, 40, "lyr_action_tokens", obj_action_bridge_blue) ) {
	team = obj_game.blueTeam;
}

//// Spawn figures red
//with( instance_create_layer(40, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.redTeam;
//}
//with( instance_create_layer(70, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.redTeam;
//}
//with( instance_create_layer(100, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.redTeam;
//}

//// Spawn figures blue
//with( instance_create_layer(880, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.blueTeam;
//}
//with( instance_create_layer(910, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.blueTeam;
//}
//with( instance_create_layer(940, 40, "lyr_action_tokens", obj_action_spawn) ) {
//	team = obj_game.blueTeam;
//}



// Create a sort depth to start with
startDepth = 0;
with( obj_action_token) {
	depth = other.startDepth;
	other.startDepth++;
}