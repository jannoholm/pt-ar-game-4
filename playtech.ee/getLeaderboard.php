<?php

$leaderboard_file = 'leaderboard.txt';
$result="";
if (file_exists($leaderboard_file)) {
	$result = file_get_contents($leaderboard_file);
}
if (strlen($result) > 20) {
	echo $result;
} else {
	echo "{\"data\":[{\"name\":\"empty\",\"position\":\"0\",\"eloRating\":\"0\",\"matches\":\"1\",\"wins\":\"0\",\"goals\":\"0\",\"bulletHits\":\"0\"}]}";
}
?>