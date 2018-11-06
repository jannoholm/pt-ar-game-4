/// @description Increase BG level
currentBackgroundLevel = clamp( currentBackgroundLevel + 0.05, 0, 1 );

audio_sound_gain( snd_background_tense,	currentBackgroundLevel, 0 );

showSoundLevelInfo = true;
alarm[0] = room_speed*2;