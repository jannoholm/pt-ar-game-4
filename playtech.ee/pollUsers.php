<?php

// file of registered people
$users_file = 'users.txt';

// secret file to not expose publicly
$secret_file = 'secret.txt';

// check if authorized thing is polling
if (!isset($_GET["secret"])) {
	die();
} else if (file_exists($secret_file)) {
	$secret = file_get_contents($secret_file);
	if (!($_GET["secret"] === urldecode(trim($secret)))) {
		http_response_code (401);
		die();
	}
} else if (!($_GET["secret"] === "-P-HH/g)9\t};[H]p#V<[cWmAb(>@$+RsM4y#UCJ(")) {
	die();
}

// position
$position = 0;
if (isset($_GET["pos"])) {
	$position = $_GET["pos"];
}

// make sure the users file exists
if (!file_exists($users_file)) {
	file_put_contents($users_file, "", LOCK_EX);
}

// open file with exclusive lock
$fp = fopen($users_file, "r");
if (flock($fp, LOCK_EX)) {
	
	$line_nr=0;
	$people_obj = array();
	while (($line = fgets($fp)) !== false) {
		$line_nr++;
		if ($line_nr <= $position) {
			continue;
		}
        // process the line read.
		$user = json_decode($line);
		$user->position=$line_nr;
		array_push($people_obj, $user);
    }

	// flush and close
    fflush($fp);            // flush output before releasing the lock
    flock($fp, LOCK_UN);    // release the lock
} else {
    echo "Couldn't get the lock!";
}
fclose($fp);

// filter by position


// write json response
header('Content-Type: application/json');
echo "{\"data\":".json_encode($people_obj)."}";

?>