function act() {
	var player = rm.getPlayer();
	var map = player.getMap();
	
	map.killAllMonsters();

	player.message("All the boogies has been killed.");

	return true;
}