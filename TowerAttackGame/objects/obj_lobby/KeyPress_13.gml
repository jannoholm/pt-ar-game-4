/// @description Add a default player to empty spots

if( playerOneTop == noone ){
	playerOneTop = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerOneTop.playerId = 0;
	playerOneTop.playerName = "TABLE";
} else if( playerTwoTop == noone ){
	playerTwoTop = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerTwoTop.playerId = 0;
	playerTwoTop.playerName = "TABLE";
}