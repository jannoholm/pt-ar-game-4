/// @description Fight unit basic

trace( "Elite unit fighting basic unit, current HP=" + string( hp ) );

if( --hp == 0 ){
	trace( "Elite unite 0 HP left, executing parent logic" )
	event_inherited();	
} else {

	alarm_set( 5, 5 ); // After 5 frams, continue path	
}