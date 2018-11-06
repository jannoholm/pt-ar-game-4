/// @description Draw last game result

draw_set_halign( fa_center );

if( currentPhase == MainPhase.INIT ){
	draw_text_transformed( room_width/2, room_height/2, "CONNECTING...", 4, 4, 0 );
} else if( currentPhase == MainPhase.GAME_RESULT ){
	
	draw_set_font( fnt_game_result );
	if( lastGameScore.winnerTeam == WinnerTeam.ONE ){
		draw_text( room_width/2, room_height*0.22, "HUMANS WIN" );
	} else if( lastGameScore.winnerTeam == WinnerTeam.TWO ) {
		draw_text( room_width/2, room_height*0.22, "ORCS WIN" );
	} else {
		draw_text( room_width/2, room_height*0.22, "IT'S A DRAW" );
	}
	draw_set_font( -1 );
	
	var team = lastGameScore.teamOne;
	draw_sprite_ext( spr_score_background, 0, room_width*0.25, room_height*0.25+40, 1, 1.4, 0, c_white, 0.65 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 100, lastGameScore.teamOne.playerName, 4, 4, 0 );
	var teamScore = 7845;
	
	var leaderBoardPosition = 0;
	var leaderBoardChange = 0;
	var eloRating = 0;
	var elaRatingChage = 0;
	
	var sgnLdrb = leaderBoardChange >= 0 ? "+" : ""; 
	var sgnElo = elaRatingChage >= 0 ? "+" : "";
	
	// 2 positions for leaderboard and 2 positions for leaderboard change
	var whitespace = string_repeat(" ", 4 - string_length( string( leaderBoardPosition ) ) - string_length( string( leaderBoardChange ) ) );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 160, "Leaderboard:       " + whitespace + string( leaderBoardPosition ) + "(" + sgnLdrb + string( leaderBoardChange ) + ")", 4, 4, 0 );
	
	// 4 positions for ELO rating and 4 positions for change
	var whitespace = string_repeat(" ", 8 - string_length( string( eloRating ) ) - string_length( string( elaRatingChage ) ) );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 220, "ELO rating:    " + whitespace + string( eloRating ) + "(" + sgnLdrb + string( elaRatingChage ) + ")", 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 280, "-------------------------", 4, 4, 0 );
	
	draw_text_transformed( room_width*0.25, room_height*0.25 + 340, "Bridges built:       " + string_format( team.bridgesBuilt, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 400, "Bridges destroyed:   " + string_format( team.bridgesDestroyed, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 460, "Own soldiers saved:  " + string_format( team.bridgeSoldierSaves, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 520, "Own soldiers fell:   " + string_format( team.bridgeSoldierDeaths, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 580, "Enemy soldiers saved:" + string_format( team.bridgeSoldierEnemySaves, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 640, "Enemy soldiers fell: " + string_format( team.bridgeSoldierEnemyKills, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.25, room_height*0.25 + 700, "TOTOAL SCORE:        " + string_format( teamScore, 4, 0 ), 4, 4, 0 );
	
	var team = lastGameScore.teamTwo;
	draw_sprite_ext( spr_score_background, 0, room_width*0.75, room_height*0.25+40, 1, 1.4, 0, c_white, 0.65 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 100, lastGameScore.teamTwo.playerName, 4, 4, 0 );
	var teamScore = 311;
	
	var leaderBoardPosition = 0;
	var leaderBoardChange = 0;
	var eloRating = 0;
	var elaRatingChage = 0;
	
	var sgnLdrb = leaderBoardChange >= 0 ? "+" : ""; 
	var sgnElo = elaRatingChage >= 0 ? "+" : "";
	
	// 2 positions for leaderboard and 2 positions for leaderboard change
	var whitespace = string_repeat(" ", 4 - string_length( string( leaderBoardPosition ) ) - string_length( string( leaderBoardChange ) ) );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 160, "Leaderboard:       " + whitespace + string( leaderBoardPosition ) + "(" + sgnLdrb + string( leaderBoardChange ) + ")", 4, 4, 0 );
	
	// 4 positions for ELO rating and 4 positions for change
	var whitespace = string_repeat(" ", 8 - string_length( string( eloRating ) ) - string_length( string( elaRatingChage ) ) );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 220, "ELO rating:    " + whitespace + string( eloRating ) + "(" + sgnLdrb + string( elaRatingChage ) + ")", 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 280, "-------------------------", 4, 4, 0 );
	
	draw_text_transformed( room_width*0.75, room_height*0.25 + 340, "Bridges built:       " + string_format( team.bridgesBuilt, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 400, "Bridges destroyed:   " + string_format( team.bridgesDestroyed, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 460, "Own soldiers saved:  " + string_format( team.bridgeSoldierSaves, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 520, "Own soldiers fell:   " + string_format( team.bridgeSoldierDeaths, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 580, "Enemy soldiers saved:" + string_format( team.bridgeSoldierEnemySaves, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 640, "Enemy soldiers fell: " + string_format( team.bridgeSoldierEnemyKills, 4, 0 ), 4, 4, 0 );
	draw_text_transformed( room_width*0.75, room_height*0.25 + 700, "TOTOAL SCORE:        " + string_format( teamScore, 4, 0 ), 4, 4, 0 );
	
} else if( currentPhase == MainPhase.DEMO ){
	draw_set_font( fnt_game_result );
	draw_text( room_width/2, room_height*0.22, "WAITING PLAYERS" );
	draw_set_font( -1 );
}