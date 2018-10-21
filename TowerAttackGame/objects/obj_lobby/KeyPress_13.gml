/// @description Add a default user to empty spots

if( userRedOne == noone ){
	userRedOne = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_user );
	userRedOne.userId = 0;
	userRedOne.userName = "TABLE";
}
if( userRedTwo == noone ){
	userRedTwo = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_user );
	userRedTwo.userId = 0;
	userRedTwo.userName = "TABLE";
}
if( userBlueOne == noone ){
	userBlueOne = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_user );
	userBlueOne.userId = 0;
	userBlueOne.userName = "TABLE";
}
if( userBlueTwo == noone ){
	userBlueTwo = instance_create_layer( 0, 0, "lyr_lobby", obj_lobby_user );
	userBlueTwo.userId = 0;
	userBlueTwo.userName = "TABLE";
}
