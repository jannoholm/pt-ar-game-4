/// @description Insert description here
// You can write your code in this editor

if( currentPhase = LobbyPhase.WAITING_PLAYERS ){
	
	instance_destroy(playerOneTop);
	instance_destroy(playerTwoTop);
	playerOneTop = noone;
	playerTwoTop = noone;
}

		