/// @description Warning trigger

for(var i = 0; i < ds_list_size( nextWaveTasks ); i++ ){
	var task = ds_list_find_value( nextWaveTasks, i );
	task.spawn.showWarningIcon = true;
}