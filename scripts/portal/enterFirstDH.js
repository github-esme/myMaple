var mapp = -1;
var map = 0;
function enter(pi) {
    if (pi.isQuestStarted(20701)) {
    	map = 913000000;
    } else if (pi.isQuestStarted(20702)) {
    	map = 913000100;
    } else if (pi.isQuestStarted(20703)) {
    	map = 913000200;
    }
    if (map > 0) {
	if (pi.getPlayerCount(map) == 0) {
	    var mapp = pi.getMap(map);
	    pi.warp(map, 0);
        return true;
	} else {
	    pi.message("Someone is already in this map.");
        return false;
	}
    } else {
    	pi.message("Hall #1 can only be entered if you're engaged in Kiku's Acclimation Training.");
        return false;
    }
}