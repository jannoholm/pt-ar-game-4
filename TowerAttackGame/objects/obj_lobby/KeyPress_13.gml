/// @description Add a default player to empty spots

if( playerRedOne == noone ){
	playerRedOne = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerRedOne.playerId = 0;
	playerRedOne.playerName = "TABLE";
}
if( playerRedTwo == noone ){
	playerRedTwo = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerRedTwo.playerId = 0;
	playerRedTwo.playerName = "TABLE";
}
if( playerBlueOne == noone ){
	playerBlueOne = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerBlueOne.playerId = 0;
	playerBlueOne.playerName = "TABLE";
}
if( playerBlueTwo == noone ){
	playerBlueTwo = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_player );
	playerBlueTwo.playerId = 0;
	playerBlueTwo.playerName = "TABLE";
}
