{
	var top_object = argument0;
	var object_type = argument1;

	if (instance_exists(object_type))
	{
		if (top_object.depth == 0)
			exit;

		top_object.depth = 0;
		with(object_type)
		{
			if (id == top_object)
				continue;
	
			depth++;
		}
	}
}