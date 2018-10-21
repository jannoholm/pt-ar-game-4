// HostTableGameRequest

// reset buffer
var buffer=obj_server_client.out_buffer;
buffer_seek(buffer, buffer_seek_start, 0);
buffer_seek(buffer, buffer_seek_end, 0);

// write content
obj_server_client.messageid_counter+=10000;
scr_write_messageheader(buffer, 2011, obj_server_client.messageid_counter, obj_server_client.client_id);

// send message
scr_send_packet(buffer);

trace( "Sent HostTableGameRequest" );