/// @description Insert description here
// You can write your code in this editor

if ( durability > 0 ) {
	
	// Roadblock is fixed, degrade if soldier passes

	var indx = ds_list_find_index(other.bridgesCrossed, self);

	if ( indx == -1 ) {
		// First time hitbox collision
		durability = durability - 10;
		ds_list_add(other.bridgesCrossed, self);
	}

} else {

	// Roadblock is active, fill it up with a body
	fillUp = fillUp + 1;	

	// Destory the colliding soldior
	// #TODO: Play animation
	instance_destroy(other);

}