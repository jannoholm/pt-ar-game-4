/// @description Hit the tower

instance_destroy();

with ( other ) {
	towerHealth = clamp( towerHealth - other.damageToTower, 0, 10000 );
}