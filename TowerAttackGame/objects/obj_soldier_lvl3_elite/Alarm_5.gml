/// @description Resume path

var prevPathPosition = path_positionprevious;
path_start(path, pathDirection * pathMoveSpeed, path_action_stop, true);
path_position = prevPathPosition;

currentPhase = SoldierPhase.FOLLOW_PATH;