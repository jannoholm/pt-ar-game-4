// GameResultStoreResponse
var buffer=argument[0];

var nrOfUsers = buffer_read( buffer, buffer_s32 );

trace( "Received GameResultStoreResponse", nrOfUsers );

for( var i = 0; i < nrOfUsers; i++ ){

	var teamId = buffer_read( buffer, buffer_s8 );
	var positionInTeam = buffer_read( buffer, buffer_s8 );
	var scoreValue = buffer_read( buffer, buffer_s32 ); // 'score' in the API
	var eloRating = buffer_read( buffer, buffer_s32 );
	var leaderboardPosition = buffer_read( buffer, buffer_s32 );
	var eloRatingChange = buffer_read( buffer, buffer_s32 );
	var leaderboardPositionChange = buffer_read( buffer, buffer_s32 );
	
	trace( "New score" , teamId, positionInTeam, scoreValue, eloRating, leaderboardPosition, eloRatingChange, leaderboardPositionChange );
	
	var team = noone;
	if( teamId == TeamId.ONE ) {
		team = obj_tower_attack.lastGameScore.teamOne;
	} else if( teamId == TeamId.TWO ) {
		team = obj_tower_attack.lastGameScore.teamTwo;
	} else {
		trace("Failed to identify team ID", teamId );
		return;
	}
	
	team.scoreValue = scoreValue;
	team.eloRating = eloRating;
	team.leaderboardPosition = leaderboardPosition;
	team.eloRatingChange = eloRatingChange;
	team.leaderboardPositionChange = leaderboardPositionChange;
}

obj_tower_attack.currentPhase = MainPhase.GAME_RESULT;