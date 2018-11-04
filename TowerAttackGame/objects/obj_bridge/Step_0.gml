/// @description Fill up counter

if ( fillUp >= 99999 ) { // TODO: In case of A.I. add this feature
	fillUp = 0;
	durability = 100;
	// Bridge is fixed by filled bodies, reset last activation if any
	activatedByTeam = noone;
}