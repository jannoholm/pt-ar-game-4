<?php

$leaderboard_file = 'leaderboard.txt';
$result="";
if (file_exists($leaderboard_file)) {
	$result = file_get_contents($leaderboard_file);
}
if (strlen($result) > 20) {
	echo $result;
} else {
	echo "{\"data\":[{\"name\":\"\",\"position\":\"\",\"eloRating\":\"\",\"matches\":\"1\",\"wins\":\"0\",\"towerHealth\":\"0\",\"enemyTowerHealth\":\"0\",\"totalScore\":\"0\"}]}";
}
?>