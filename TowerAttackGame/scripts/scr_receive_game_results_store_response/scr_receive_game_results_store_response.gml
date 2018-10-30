// GameResultStoreResponse
var buffer=argument[0];

var nrOfUsers = buffer_read( buffer, buffer_s32 );

trace( "Received GameResultStoreResponse", nrOfUsers );

for( var i = 0; i < nrOfUsers; i++ ){

	var team = buffer_read( buffer, buffer_s8 );
	var positionInTeam = buffer_read( buffer, buffer_s8 );
	var scoreValue = buffer_read( buffer, buffer_s32 ); // 'score' in the API
	var eloRating = buffer_read( buffer, buffer_s32 );
	var leaderboardPosition = buffer_read( buffer, buffer_s32 );
	var eloRatingChange = buffer_read( buffer, buffer_s32 );
	var leaderboardPositionChange = buffer_read( buffer, buffer_s32 );
	
	trace( "New score" , team, positionInTeam, scoreValue, eloRating, leaderboardPosition, eloRatingChange, leaderboardPositionChange );
}
