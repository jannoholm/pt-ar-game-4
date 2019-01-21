/// @description Draw last game result

draw_set_halign( fa_center );

if( currentPhase == MainPhase.INIT ){
	draw_text_transformed( room_width/2, room_height/2, "CONNECTING...", 4, 4, 0 );
} else if( currentPhase == MainPhase.GAME_RESULT ){
	
	draw_set_font( fnt_basic_font );
	if( lastGameScore.winnerTeam == WinnerTeam.ONE ){
		draw_text( room_width/2, room_height*0.23, lastGameScore.teamOne.playerName + " WINS" );
	} else if( lastGameScore.winnerTeam == WinnerTeam.TWO ) {
		draw_text( room_width/2, room_height*0.23, lastGameScore.teamTwo.playerName + " WINS" );
	} else {
		draw_text( room_width/2, room_height*0.23, "IT'S A DRAW" );
	}

	draw_set_font( -1 );
	
	draw_set_font( fnt_game_result_data );
	var team = lastGameScore.teamOne;
	draw_sprite_ext( spr_score_background, 0, room_width*0.25, room_height*0.25+40, 1, 1.4, 0, c_white, 0.65 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 100, lastGameScore.teamOne.playerName, 1, 1, 0 );
	
	var sgnLdrb = team.leaderboardPositionChange >= 0 ? "+" : ""; 
	var sgnElo = team.eloRatingChange >= 0 ? "+" : "";
	
	draw_set_halign( fa_left );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 160, "Leaderboard:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 220, "ELO rating:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 280, "---------------------------------------------------", 1, 1, 0 );
	
	draw_text_transformed( room_width*0.13, room_height*0.25 + 340, "Bridges built:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 400, "Bridges destroyed:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 460, "Own soldiers saved:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 520, "Own soldiers fell:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 580, "Enemy soldiers saved:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 640, "Enemy soldiers fell:", 1, 1, 0 );
	draw_text_transformed( room_width*0.13, room_height*0.25 + 700, "TOTAL SCORE:", 1, 1, 0 );

	draw_set_halign( fa_right );
	
	draw_text_transformed( room_width*0.37, room_height*0.25 + 160, string( team.leaderboardPosition ) + " (" + sgnLdrb + string( team.leaderboardPositionChange ) + ")", 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 220, string( team.eloRating ) + " (" + sgnLdrb + string( team.eloRatingChange ) + ")", 1, 1, 0 );

	draw_text_transformed( room_width*0.37, room_height*0.25 + 340, string_format( team.bridgesBuilt, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 400, string_format( team.bridgesDestroyed, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 460, string_format( team.bridgeSoldierSaves, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 520, string_format( team.bridgeSoldierDeaths, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 580, string_format( team.bridgeSoldierEnemySaves, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 640, string_format( team.bridgeSoldierEnemyKills, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.37, room_height*0.25 + 700, string_format( team.scoreValue, 4, 0 ), 1, 1, 0 );
	draw_set_halign( fa_center );
	
	var team = lastGameScore.teamTwo;
	draw_sprite_ext( spr_score_background, 0, room_width*0.75, room_height*0.25+40, 1, 1.4, 0, c_white, 0.65 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 100, lastGameScore.teamTwo.playerName, 1, 1, 0 );
	
	var sgnLdrb = team.leaderboardPositionChange >= 0 ? "+" : ""; 
	var sgnElo = team.eloRatingChange >= 0 ? "+" : "";
	
	draw_set_halign( fa_left );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 160, "Leaderboard:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 220, "ELO rating:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 280, "---------------------------------------------------", 1, 1, 0 );
	
	draw_text_transformed( room_width*0.63, room_height*0.25 + 340, "Bridges built:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 400, "Bridges destroyed:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 460, "Own soldiers saved:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 520, "Own soldiers fell:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 580, "Enemy soldiers saved:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 640, "Enemy soldiers fell:", 1, 1, 0 );
	draw_text_transformed( room_width*0.63, room_height*0.25 + 700, "TOTAL SCORE:", 1, 1, 0 );

	draw_set_halign( fa_right );
	
	draw_text_transformed( room_width*0.87, room_height*0.25 + 160, string( team.leaderboardPosition ) + " (" + sgnLdrb + string( team.leaderboardPositionChange ) + ")", 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 220, string( team.eloRating ) + " (" + sgnLdrb + string( team.eloRatingChange ) + ")", 1, 1, 0 );

	draw_text_transformed( room_width*0.87, room_height*0.25 + 340, string_format( team.bridgesBuilt, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 400, string_format( team.bridgesDestroyed, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 460, string_format( team.bridgeSoldierSaves, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 520, string_format( team.bridgeSoldierDeaths, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 580, string_format( team.bridgeSoldierEnemySaves, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 640, string_format( team.bridgeSoldierEnemyKills, 4, 0 ), 1, 1, 0 );
	draw_text_transformed( room_width*0.87, room_height*0.25 + 700, string_format( team.scoreValue, 4, 0 ), 1, 1, 0 );
	draw_set_halign( fa_center );
	
	draw_set_font( -1 );
	
} else if( currentPhase == MainPhase.DEMO ){
	draw_set_font( fnt_basic_font );
	//draw_text( room_width/2, room_height*0.22, "WAITING PLAYERS" );
	draw_text_transformed( room_width*0.51, room_height*0.21, "REGISTER @ TOWER.PLAYTECH.EE", 0.9, 0.9, 0);
	draw_text_transformed( room_width*0.5, room_height*0.25, "Waiting for players", 0.75, 0.75, 0 );
	draw_set_font( -1 );
}

