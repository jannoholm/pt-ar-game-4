// GameResultStoreRequest 
var result = argument[0];

// reset buffer
var buffer=obj_server_client.out_buffer;
buffer_seek( buffer, buffer_seek_start, 0 );
buffer_seek( buffer, buffer_seek_end, 0 );

// write content
obj_server_client.messageid_counter+=10000;
scr_write_messageheader( buffer, 3014, obj_server_client.messageid_counter, obj_server_client.client_id );
buffer_write( buffer, buffer_string, obj_server_client.gameId );
buffer_write( buffer, buffer_s8, result.winnerTeam );
buffer_write( buffer, buffer_s32, result.gameTime ); // TODO: FIX to seconds
buffer_write( buffer, buffer_s32, 2 ); // Number of players

// GameResultPlayerActivity
buffer_write( buffer, buffer_s32, result.redTeam.userId );
buffer_write( buffer, buffer_s8, result.redTeam.teamId );
buffer_write( buffer, buffer_s8, result.redTeam.positionInTeam );
buffer_write( buffer, buffer_s32, result.redTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.blueTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.redTeam.bridgesBuilt );
buffer_write( buffer, buffer_s32, result.redTeam.bridgesBuiltPoints );
buffer_write( buffer, buffer_s32, result.redTeam.bridgesDestroyed );
buffer_write( buffer, buffer_s32, result.redTeam.bridgesDestroyedPoints );
buffer_write( buffer, buffer_s32, result.redTeam.bridgeSoldierSaves );
buffer_write( buffer, buffer_s32, result.redTeam.bridgeSoldierDeaths );
buffer_write( buffer, buffer_s32, result.redTeam.bridgeSoldierEnemySaves );
buffer_write( buffer, buffer_s32, result.redTeam.bridgeSoldierEnemyKills );

// GameResultPlayerActivity
buffer_write( buffer, buffer_s32, result.blueTeam.userId );
buffer_write( buffer, buffer_s8, result.blueTeam.teamId );
buffer_write( buffer, buffer_s8, result.blueTeam.positionInTeam );
buffer_write( buffer, buffer_s32, result.blueTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.redTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.blueTeam.bridgesBuilt );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgesBuiltPoints );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgesDestroyed );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgesDestroyedPoints );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgeSoldierSaves );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgeSoldierDeaths );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgeSoldierEnemySaves );
buffer_write( buffer, buffer_s32, result.blueTeam.bridgeSoldierEnemyKills );


// send message
scr_send_packet(buffer);

