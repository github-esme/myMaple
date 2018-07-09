function act(){
	var player = rm.getPlayer();
	var map = player.getMap();
	
	map.killAllMonsters();

	player.message("Rurumo has been killed.");

	return true;
}