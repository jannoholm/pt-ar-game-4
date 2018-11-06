
var totalLength = argument0;
var firstNumber = argument1;
var secondNumber = argument2;

var whitespaceNeeded = totalLength - string_length( string( firstNumber ) ) - string_length( string( secondNumber ) );

var whitespace = "";
	
for( var i = 0; i < spacer; i++ ){
	whitespace += " ";	
}

return whitespace;