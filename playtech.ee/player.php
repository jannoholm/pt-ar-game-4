<?php
// file of registered people
$people_file = 'users.txt';

// make sure the file exists
if (!file_exists($people_file)) {
	file_put_contents($people_file, "", LOCK_EX);
}

// open file with exclusive lock
$fp = fopen($people_file, "a");
if (flock($fp, LOCK_EX)) {
	
	// add new data to existing
	$user = new class{};
	$user->name = $_POST["name"];
	$user->email = $_POST["email"];
	$user->qrCode = base64_encode(mt_rand(10000000,19999999));
	
	// write new file content
	fwrite($fp, json_encode($user));
	fwrite($fp, "\n");

	// flush and close
    fflush($fp);            // flush output before releasing the lock
    flock($fp, LOCK_UN);    // release the lock
} else {
    echo "Couldn't get the lock!";
}
fclose($fp);
?>