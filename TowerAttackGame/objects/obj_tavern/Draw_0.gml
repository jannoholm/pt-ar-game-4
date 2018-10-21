/// @description Insert description here
// You can write your code in this editor

draw_self();

var drawX = room_width * 0.35;
var drawY = 5;

for( var soldier = ds_map_find_first( soldiersInTavern ); !is_undefined( soldier ); soldier = ds_map_find_next( soldiersInTavern, soldier ) ) {

	var currentCounter = soldiersInTavern[? soldier];
	
	draw_text(drawX, drawY, "" + string( soldier ) + "=" + string( currentCounter ) );	

	drawY += 20;
}