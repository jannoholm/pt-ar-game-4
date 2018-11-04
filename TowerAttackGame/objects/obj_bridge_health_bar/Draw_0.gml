/// @description Hide when not active

if( targetBridge.durability > 0 && obj_game.currentPhase == GamePhase.GAME ){
	draw_self();
}