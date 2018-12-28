<?php
// file of registered people
$people_file = 'users.txt';

// make sure the file exists
if (!file_exists($people_file)) {
	file_put_contents($people_file, "", LOCK_EX);
}

$result_user=new class{};
$result_user->name = trim(strtoupper($_POST["name"]));
$result_user->email = trim(strtoupper($_POST["email"]));
$result_user->qrCode = "";

// open file with exclusive lock
$fp = fopen($people_file, "r");
if (flock($fp, LOCK_EX)) {
	
	while (($line = fgets($fp)) !== false) {
		$user = json_decode($line);
		if ($user->email==$result_user->email) {
			$result_user->name=$user->name;
			$result_user->qrCode=$user->qrCode;
			break;
		}
    }

	// flush and close
    fflush($fp);            // flush output before releasing the lock
    flock($fp, LOCK_UN);    // release the lock
} else {
    echo "Couldn't get the lock!";
}
fclose($fp);

if (strlen($result_user->qrCode) == 0) {
	// open file with exclusive lock
	$fp = fopen($people_file, "a");
	if (flock($fp, LOCK_EX)) {
		
		// add new data to existing
		$user = new class{};
		$user->name = $result_user->name;
		$user->email = $result_user->email;
		$result_user->qrCode=base64_encode(mt_rand(10000000,19999999));
		$user->qrCode = $result_user->qrCode;
		
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
}

// set cookie
setcookie("towerAttack", $result_user->qrCode, time() + (86400 * 30), "/"); // 86400 = 1 day

echo json_encode($result_user);

?>