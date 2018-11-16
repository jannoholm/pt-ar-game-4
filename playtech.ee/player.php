<?php
// file of registered people
$people_file = 'users.txt';

// make sure the file exists
if (!file_exists($people_file)) {
	file_put_contents($people_file, "", LOCK_EX);
}

// open file with exclusive lock
$fp = fopen($people_file, "r+");
if (flock($fp, LOCK_EX)) {
	
	// read file contents
	if (filesize($people_file) > 0) {
		$people_json = fread($fp,filesize($people_file));
		$people_obj = json_decode($people_json);
	} else {
		$people_obj = array();
	}
	
	// add new data to existing
	$user = new class{};
	$user->name = $_POST["name"];
	$user->email = $_POST["email"];
	$user->position = count($people_obj)+1; 
	array_push($people_obj, $user);
	
	// write new file content
	ftruncate($fp, 0);
	rewind($fp);
	fwrite($fp, json_encode($people_obj));

	// flush and close
    fflush($fp);            // flush output before releasing the lock
    flock($fp, LOCK_UN);    // release the lock
} else {
    echo "Couldn't get the lock!";
}
fclose($fp);
?>