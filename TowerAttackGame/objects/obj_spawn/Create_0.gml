/// @description Init

team = noone;
path = noone;
pathDirection = 1;
pathStartPosition = 0.05;

spawnDelay = room_speed / 2;
spawnDelayAlarmIndx = 0;
spawnDelayCleared = true; // As soon as activated, spawn a soldier

spawnTasks = ds_list_create();

showWarningIcon = false;
warningIconShrink = true;
warningIconSize = 0.6; // Add a little wobble effect