// SetUserOnMapResponse

// reset buffer
var buffer=obj_server_client.out_buffer;
buffer_seek(buffer, buffer_seek_start, 0);
buffer_seek(buffer, buffer_seek_end, 0);

// write content
obj_server_client.messageid_counter+=10000;
scr_write_messageheader(buffer, 3101, obj_server_client.messageid_counter, obj_server_client.client_id);
buffer_write(buffer, buffer_s32, 0); // Error code
buffer_write(buffer, buffer_string, ""); // Error text

// send message
scr_send_packet(buffer);

trace( "Sent SetUserOnMapResponse" );