/// scr_receive_packet
var buffer = argument[0];

// parse header
// read byte count of header
var packet_size = buffer_read( buffer, buffer_s32 );
var header_size = buffer_read( buffer, buffer_s32 );
var message_type = buffer_read( buffer, buffer_s32 );
var message_id = buffer_read( buffer, buffer_u64 );
var client_id = buffer_read( buffer, buffer_string );

trace( "IN: ", packet_size, header_size, message_type, message_id, client_id );

switch( message_type ) {
	case 1000: // ping request
		// send ping response
		scr_write_messageheader( obj_server_client.out_buffer, 1001, message_id, client_id );
		scr_send_packet( obj_server_client.out_buffer );
		obj_server_client.last_ping=current_time;
		show_debug_message( "ping processed" );
		break;
	case 1003: // join server response
		var errorCode = buffer_read( buffer, buffer_s32 );
		var errorMessage = buffer_read( buffer, buffer_string );
		show_debug_message( "got join response: " + client_id + ", " + string( errorCode ) + ":" + errorMessage );
		if( errorCode != 0 ){
			show_message( "Unable to join server: " + string( errorCode ) + " " + error_message );
		} else {
			trace( "Received successful server join response" );
			scr_send_host_table_game();
		}
		break;
	case 2012: // HostTableGameResponse
		var errorCode = buffer_read( buffer, buffer_s32 );
		var errorMessage = buffer_read( buffer, buffer_string );
		if( errorCode != 0 ) {
			show_message( "Unable to host table game: " + string( errorCode ) + " " + errorMessage );
		} else {
			scr_receive_host_table_game(buffer);
		}
		
		break;
	case 3015: // GameResultStoreResponse
		var errorCode = buffer_read( buffer, buffer_s32 );
		var errorMessage = buffer_read( buffer, buffer_string );
		if( errorCode != 0 ) {
			show_message( "Failed to store game result: " + string( errorCode ) + " " + errorMessage );
		} else {
			scr_receive_game_results_store_response( buffer );
		}
		break;
	case 3100: // SetUserOnMapRequest
		scr_receive_set_user_on_map(buffer);
		break;		
	case 5000: // TokenLocationUpdateMessage
		scr_receive_token_location_update( buffer );
		break;
	default:
		trace( "Received unexpected command", message_type );
}
