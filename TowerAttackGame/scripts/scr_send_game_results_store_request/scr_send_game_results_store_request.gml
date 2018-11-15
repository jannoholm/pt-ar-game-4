// GameResultStoreRequest 
var result = argument[0];

trace( "Sending GameResultStoreRequest" );

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
buffer_write( buffer, buffer_s32, result.teamOne.playerId );
buffer_write( buffer, buffer_s8, result.teamOne.teamId );
buffer_write( buffer, buffer_s8, result.teamOne.positionInTeam );
buffer_write( buffer, buffer_s32, result.teamOneTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.teamTwoTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.teamOne.bridgesBuilt );
buffer_write( buffer, buffer_s32, result.teamOne.bridgesBuiltPoints );
buffer_write( buffer, buffer_s32, result.teamOne.bridgesDestroyed );
buffer_write( buffer, buffer_s32, result.teamOne.bridgesDestroyedPoints );
buffer_write( buffer, buffer_s32, result.teamOne.bridgeSoldierSaves );
buffer_write( buffer, buffer_s32, result.teamOne.bridgeSoldierDeaths );
buffer_write( buffer, buffer_s32, result.teamOne.bridgeSoldierEnemySaves );
buffer_write( buffer, buffer_s32, result.teamOne.bridgeSoldierEnemyKills );

// GameResultPlayerActivity
buffer_write( buffer, buffer_s32, result.teamTwo.playerId );
buffer_write( buffer, buffer_s8, result.teamTwo.teamId );
buffer_write( buffer, buffer_s8, result.teamTwo.positionInTeam );
buffer_write( buffer, buffer_s32, result.teamTwoTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.teamOneTowerHealth ); // #TODO Make dynamic
buffer_write( buffer, buffer_s32, result.teamTwo.bridgesBuilt );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgesBuiltPoints );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgesDestroyed );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgesDestroyedPoints );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgeSoldierSaves );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgeSoldierDeaths );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgeSoldierEnemySaves );
buffer_write( buffer, buffer_s32, result.teamTwo.bridgeSoldierEnemyKills );


// send message
scr_send_packet(buffer);

