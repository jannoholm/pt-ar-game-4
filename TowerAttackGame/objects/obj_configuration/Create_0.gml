/// @description default values
ip_address = "localhost";
port = 8100;
standAloneMode = 0;

// overwrite from file
var filename = working_directory + "conf.txt";

if( file_exists( filename ) ){
	trace( "Reading file", filename);
	var filehandle = file_text_open_read( filename );
	ip_address = file_text_read_string( filehandle ); file_text_readln( filehandle );
	port = file_text_read_real( filehandle ); file_text_readln( filehandle );
	standAloneMode = file_text_read_real( filehandle ); file_text_readln( filehandle );
	file_text_close( filehandle );
	trace( "Processing file complete");
}

show_debug_message( "Using ip: " + ip_address + ":" + string( port ) );
show_debug_message( "Using standaolne mode: " + string( standAloneMode ) );
