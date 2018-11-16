<?php
if ($_POST['data'])
{
  file_put_contents('leaderboard.txt', $_POST['data'], LOCK_EX);
  echo 'OK';
} else {
  echo 'No data';    
}
?>