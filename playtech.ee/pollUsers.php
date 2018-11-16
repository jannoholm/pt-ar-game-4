<?php

// file of registered people
$people_file = 'users.txt';

// check if authorized thing is polling
if (!(isset($_GET["secret"]) && $_GET["secret"] === "-P-HH/g)9\t};[H]p#V<[cWmAb(>@$+RsM4y#UCJ(")) {
	die();
}

// make sure the file exists
if (!file_exists($people_file)) {
	file_put_contents($people_file, "", LOCK_EX);
}

// open file with exclusive lock
$fp = fopen($people_file, "r");
if (flock($fp, LOCK_EX)) {
	
	// read file contents
	if (filesize($people_file) > 0) {
		$people_json = fread($fp,filesize($people_file));
		$people_obj = json_decode($people_json);
	} else {
		$people_obj = array();
	}

	// flush and close
    fflush($fp);            // flush output before releasing the lock
    flock($fp, LOCK_UN);    // release the lock
} else {
    echo "Couldn't get the lock!";
}
fclose($fp);

// write json response
header('Content-Type: application/json');
echo "{\"data\":".json_encode($people_obj)."}";

?>