/// @description default values
ip_address = "localhost";
port=8100;
client_type=0;
reuse_clientid=true;

// overwrite from file
var filename=working_directory + "/conf.txt";
if (file_exists(filename)) {
	var filehandle = file_text_open_read(filename);
	ip_address=file_text_read_string(filehandle);file_text_readln(filehandle);
	port=file_text_read_string(filehandle);file_text_readln(filehandle);
	client_type=real(file_text_read_string(filehandle));file_text_readln(filehandle);
	reuse_clientid=real(file_text_read_string(filehandle));
	file_text_close(filehandle);
}

show_debug_message("Using ip: " + ip_address + ":" + string(port));
show_debug_message("client_type: " + string(client_type));
