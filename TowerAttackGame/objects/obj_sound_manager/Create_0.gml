/// @description Sound levels

showSoundLevelInfo = false;
currentBackgroundLevel = 0.4;

audio_sound_gain( snd_background_tense,	currentBackgroundLevel, 0 );
audio_sound_gain( snd_bridge_built,		0.9, 0 );
audio_sound_gain( snd_bridge_destroyed, 0.9, 0 );
audio_sound_gain( snd_explosion_big,	0.5, 0 );
audio_sound_gain( snd_next_wave,		1.0, 0 );
audio_sound_gain( snd_tower_hit,		0.9, 0 );
audio_sound_gain( snd_unit_hit,			0.5, 0 );
audio_sound_gain( snd_victory_music,	0.5, 0 );