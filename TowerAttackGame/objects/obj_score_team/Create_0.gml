/// @description Initial values

teamId = noone;
playerId = noone;
playerName = noone;
positionInTeam = 0; // #TODO Start tracking per player if it will be 2v2

// Current assumption is 1v1
// #TODO Start tracking per player if it will be 2v2
bridgesBuilt = 0;
bridgesBuiltPoints = 0;
bridgesDestroyed = 0;
bridgesDestroyedPoints = 0;
bridgeSoldierSaves = 0;
bridgeSoldierDeaths = 0;
bridgeSoldierEnemySaves = 0;
bridgeSoldierEnemyKills = 0;

tavernSoldierDeaths = 0;
tavernSoldierKills = 0;

// Set by server
scoreValue = 1234; // 'score' in the API
eloRating = 1000;
leaderboardPosition = 12;
eloRatingChange = -23;
leaderboardPositionChange = -1;