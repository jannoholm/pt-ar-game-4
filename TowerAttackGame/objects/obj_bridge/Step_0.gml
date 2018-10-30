/// @description Insert description here
// You can write your code in this editor

if ( fillUp >= 10 ) {
	fillUp = 0;
	durability = 100;
	// Bridge is fixed by filled bodies, reset last activation if any
	activatedByTeam = noone;
}

if( shield > 0) {
	shield--;
}