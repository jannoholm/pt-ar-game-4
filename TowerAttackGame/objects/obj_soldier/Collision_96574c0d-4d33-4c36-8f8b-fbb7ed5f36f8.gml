/// @description Hit the tower

with ( other ) {
	towerHealth = clamp( towerHealth - other.damageToTower, 0, 10000 );
}

audio_play_sound( snd_tower_hit, 20, false );

instance_destroy();