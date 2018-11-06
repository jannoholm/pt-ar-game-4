/// @description Hide score
if( currentPhase == MainPhase.GAME_RESULT ){
	currentPhase = MainPhase.DEMO;
}

		
audio_stop_sound( snd_victory_music );
audio_play_sound( snd_background_tense, 10, true );